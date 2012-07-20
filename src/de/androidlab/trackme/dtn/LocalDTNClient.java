package de.androidlab.trackme.dtn;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import de.androidlab.trackme.db.LocationDatabase;
import de.androidlab.trackme.data.SettingsData;
import de.tubs.ibr.dtn.api.Block;
import de.tubs.ibr.dtn.api.Bundle;
import de.tubs.ibr.dtn.api.CallbackMode;
import de.tubs.ibr.dtn.api.DTNClient;
import de.tubs.ibr.dtn.api.DTNClient.Session;
import de.tubs.ibr.dtn.api.DataHandler;
import de.tubs.ibr.dtn.api.GroupEndpoint;
import de.tubs.ibr.dtn.api.Registration;
import de.tubs.ibr.dtn.api.ServiceNotAvailableException;
import de.tubs.ibr.dtn.api.SessionDestroyedException;
import de.tubs.ibr.dtn.api.SingletonEndpoint;


/**
 * Class for DTN communication.
 * Its not designed for reuse and works only within the TrackMe application.
 * Especially in combination with the LocationDatabase.
 * {@link LocationDatabase}
 * {@author Christian Brümmer}
 */

public class LocalDTNClient {
	
	/**
	 * Enumeration for package identification.
	 */
	enum PacketType
	{
		PRESENCE,
		DATA
	}
	
	// Global tag for LogCat
	private final static String LOGTAG = "LocalDTNClient";
	
	// Size of type identification in header
	private final static int HEADER_TYPE_SIZE = 4;
	// Size of the payload length information
	private final static int HEADER_LENGTH_SIZE = 0;
	// Entire header size
	private final static int HEADER_SIZE = HEADER_TYPE_SIZE + HEADER_LENGTH_SIZE;
	
	// Global package name
	private String mPackageName = getClass().getPackage().getName();
	
	// DTN-GroupEndpoint name for presence notification
	public static final GroupEndpoint PRESENCE_GROUP_ID = new GroupEndpoint("dtn://trackme.dtn/presence");
	
	// Application context
	private Context mContext = null;
	
	// Executor to process local job queue
	private ExecutorService mExecutor = null;
	
	// DTN-client to talk with the DTN service
	private LDTNClient mClient = null;
	
	// Interface for DB communication
	private LocationDatabase mLocationDatabase;
	
	// Timer for presence notification
	PresenceTimerTask mTask = new PresenceTimerTask();
	Timer mTimer = new Timer();
	
	// Presence organization
	Map<String, Long> mPresenceMap = new HashMap<String, Long>();
	
	// First time init flag
	boolean mInit, mClosed;
	
	/**
	 * Creates client for dtn communication.
	 * @param db reference to LocationDatabase for data exchange
	 */
	public LocalDTNClient(LocationDatabase db)
	{
		mInit = false;
		mClosed = false;
		mLocationDatabase = db;
	}
	
	/**
	 * Class which extends DTNClient and overwrite methods for communication.
	 */
	protected class LDTNClient extends DTNClient {
		
		public LDTNClient() {
			super(mPackageName);
		}

		/**
		 * Callback for sessionConnected event.
		 * @param session reference to DTN Session
		 */
		@Override
		protected void sessionConnected(Session session) {
			Log.d(LOGTAG, "DTN session connected");
			
	        // check for bundles first
	        mExecutor.execute(mQueryTask);
		}

		/**
		 * Callback for sessionMode configuration. Used the set the client CallbackMode.
		 * @param db reference to LocationDatabase for data exchange
		 */
		@Override
		protected CallbackMode sessionMode() {
			return CallbackMode.SIMPLE;
		}
		
		/**
		 * Callback for DTNClient online event.
		 */
		@Override
		protected void online() {
			Log.i(LOGTAG, "DTN is online.");
		}

		/**
		 * Callback for DTNClient.
		 * @param db reference to LocationDatabase for data exchange
		 */
		@Override
		protected void offline() {
			Log.i(LOGTAG, "DTN is offline.");
		}
		
	};
	
	/**
	 * The anonymous class extends BroadcastReceiver for receiving DTN intents.
	 * If an intent is received a executor thread (mQueryTask) will be queued in the SingleThreadExecutor.
	 * Have a look on the mQueryTask object.
	 */
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(de.tubs.ibr.dtn.Intent.RECEIVE))
			{
				Log.d(LOGTAG, "Intent received!");
				// RECEIVE intent received, check for new bundles
				if(mExecutor != null && mQueryTask != null)
					mExecutor.execute(mQueryTask);
				else
					Log.e("ERROR", "LocalDTNClient onReceive: no executor or query task available! Executor: " + mExecutor + " QueryTask " + mQueryTask);
			}
		}
	};
	
	/**
	 * The anonymous class extends Runnable and is for querying the DTNClient after an DTN intent is received.
	 * It will query the DTNClient until all data is read or an exception is thrown.
	 * Have a look on the mReceiver object.
	 */
	private Runnable mQueryTask = new Runnable() {
		@Override
		public void run() {
			try {
				while (mClient.query());
			} catch (SessionDestroyedException e) {
				Log.d(LOGTAG, null, e);
			} catch (InterruptedException e) {
				Log.d(LOGTAG, null, e);
			}
			Log.d(LOGTAG, "query for bundles done.");
		}
	};
	
	/**
	 * This method will initialize the whole DTNClient and register a BroadcastReiver for DTN intents.
	 * A new SingleThreadExecutor will be created to manage and queue all DTN tasks.
	 * A GroupEndpoint will be created for the presence communication messages.
	 * A data handle for incoming bundles will be created and attached.
	 * A presence notification timer is created and started.
	 * If the DTN-daemon is not running the initializing will fail. 
	 * @param context reference to the major application context
	 * @param packageName package name for DTN intent registration
	 * @return true if nothing goes wrong
	 */
	public boolean init(Context context, String packageName) {
		if(mInit) return true; 
		Log.i(LOGTAG, "Initialize the DTN client ...");
		Log.i(LOGTAG, "Retransmission time " + SettingsData.default_retransmission_time);
		Log.i(LOGTAG, "Presence notification delay " + SettingsData.default_presence_notification_delay);
		Log.i(LOGTAG, "Presence TTL " + SettingsData.default_presence_ttl);
		Log.i(LOGTAG, "Data TTL " + SettingsData.default_data_ttl);

		mPackageName = packageName;
        mContext = context;
        // create a new executor
        mExecutor = Executors.newSingleThreadExecutor();
        
        // create a new DTN client
        mClient = new LDTNClient();
        
        // register to RECEIVE intent
		IntentFilter receive_filter = new IntentFilter(de.tubs.ibr.dtn.Intent.RECEIVE);
		receive_filter.addCategory(mPackageName);
		Log.i(LOGTAG, "Package name: " + mPackageName);
        context.registerReceiver(mReceiver, receive_filter);
        
        // create a new registration
        Registration reg = new Registration("TrackME");
        reg.add(PRESENCE_GROUP_ID);
        
        // set the data handler for incoming bundles
        mClient.setDataHandler(mHandler);
        
		try {
			mClient.initialize(context, reg);
			Log.i(LOGTAG, "... dtn client successfully initialized");
		} catch (ServiceNotAvailableException e) {
			showInstallServiceDialog(context);
			Log.e(LOGTAG, "... dtn client exception", e);
			return false;
		}
		
		// start presence notification timer
		mTimer.scheduleAtFixedRate(mTask, 0, SettingsData.default_presence_notification_delay);
        mInit = true;
        mClosed = false;
		Log.d(LOGTAG, "DTN-Client created");
		return true;
    }

	/**
	 * Starts a install dialog if the DTN-Daemon is not installed on the target phone.
	 * @param context reference to the major application context
	 */
	private void showInstallServiceDialog(final Context context) {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
					final Intent marketIntent = new Intent(Intent.ACTION_VIEW);
					marketIntent.setData(Uri.parse("market://details?id=de.tubs.ibr.dtn"));
					context.startActivity(marketIntent);
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		            break;
		        }
		        close(context);
		    }
		};
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Missing dtn daemon!");
		builder.setPositiveButton("Yes", dialogClickListener);
		builder.setNegativeButton("No", dialogClickListener);
		builder.show();
	}
	
	/**
	 * This method closes the DTNClient and all stuff initialized before.
	 * If the init() method is not called before this does nothing.
	 * @param context reference to the major application context
	 */
	public void close(Context context)
	{
		if(mClosed || !mInit) return;
		mTimer.cancel();
		mTimer.purge();
		
		try { // unregister intent receiver
			context.unregisterReceiver(mReceiver);
		} catch(IllegalArgumentException e) {
			
		}
	
		try {
			// stop executor
			mExecutor.shutdown();
			// ... and wait until all jobs are done
			if (!mExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
				mExecutor.shutdownNow();
			}
		} catch (InterruptedException e) {
			Log.e(LOGTAG, "Interrupted on service destruction.", e);
		}
		// unregister at the daemon
		mClient.unregister();
		// destroy DTN client
		mClient.terminate();
		// clear all variables
		mExecutor = null;
		mClient = null;
		mInit = false;
		mClosed = true;
	}

	/**
	 * The anonymous class extends DataHandler and is used to process incoming DTN bundles.
	 */
   private DataHandler mHandler = new DataHandler() {
    	Bundle current;

    	/**
    	 * Callback for startBundle notification.
    	 * @param bundle current bundle
    	 */
		@Override
		public void startBundle(Bundle bundle) {
			this.current = bundle;
		}

    	/**
    	 * Callback for endBundle notification.
    	 */
		@Override
		public void endBundle() {
			
			final de.tubs.ibr.dtn.api.BundleID received = new de.tubs.ibr.dtn.api.BundleID(this.current);

			// run the queue and delivered process asynchronously
			mExecutor.execute(new Runnable() {
		        public void run() {
					try {
						mClient.getSession().delivered(received);
					} catch (Exception e) {
						Log.e(LOGTAG, "Can not mark bundle as delivered.", e);
					}
		        }
			});
			
			this.current = null;
		}

    	/**
    	 * Callback for startBlock notification.
    	 * @param bundle current block
    	 */
		@Override
		public void startBlock(Block block) {
		}

    	/**
    	 * Callback for endBlock notification.
    	 */
		@Override
		public void endBlock() {
		}

    	/**
    	 * Callback for characters.
    	 * @param data characters
    	 */
		@Override
		public void characters(String data) {
		}

    	/**
    	 * Callback for receiving bundle payload.
    	 * Read header informations and parse the payload and package type to processIncomingMessage() method.
    	 * Its important to process, create and send the reply in another thread queued in the DTN executor.
    	 * @param data whole bundle payload
    	 */
		@Override
		public void payload(byte[] data) {
			final String endpoint = current.source;
			final byte[] payload = data;
			Log.d(LOGTAG, "Incoming packet. Size: " + payload.length);
			mExecutor.execute(new Runnable() {
		        public void run() {
		        	Log.d(LOGTAG, "IN EXECUTOR 1: " + payload.length);
		        	if(payload.length >= HEADER_SIZE)
		        	{
		        		Log.d(LOGTAG, "IN EXECUTOR 2: " + payload.length);
		        		ByteBuffer bb = ByteBuffer.wrap(payload);
		        		bb.position(0);
		        		int type = bb.getInt();
		        		Log.d(LOGTAG, "IN EXECUTOR 3 type / size: " + type);
	        			byte[] pay = new byte[bb.remaining()];
	        			bb.get(pay);
	        			processIncomingMessage(PacketType.values()[type], pay, endpoint);
	        		
		        	}
		        }});
		}
		
    	/**
    	 * Main method for handle incoming payload.
    	 * This method takes payload and header information for processing incoming messages.
    	 * @param type package type
    	 * @param payload payload of the TrackMe package
    	 * @param srcEndpoint dtn source address
    	 */
		private void processIncomingMessage(PacketType type, byte[] payload, String srcEndpoint)
		{
			Log.d(LOGTAG, "PacketType: " + type.ordinal() + " Payload size: " + payload.length);
			switch(type)
			{
			case PRESENCE:
				Log.d(LOGTAG, "processIncomingMessage: PRESENCE");
				long currentTime = System.currentTimeMillis();
				boolean newEntry = false;
				if(!mPresenceMap.containsKey(srcEndpoint))
				{
					// fill in time if not existing before
					mPresenceMap.put(srcEndpoint, currentTime);
					newEntry = true;
					Log.d(LOGTAG, "processIncomingMessage: new endpoint");
				}
				Log.d(LOGTAG, "processIncomingMessage: time elapsed: " + (currentTime - mPresenceMap.get(srcEndpoint)));
				if((currentTime - mPresenceMap.get(srcEndpoint)) > SettingsData.default_retransmission_time || newEntry)
				{
					Log.d(LOGTAG, "processIncomingMessage: time elapsed !");
					// update time
					mPresenceMap.put(srcEndpoint, currentTime);
					
					// send data to endpoint!
					
					Vector<String> strings = mLocationDatabase.getDatabaseAsStrings();	// getting string vector
					if(strings.size() == 0) return;
					String result = new String();
					for(int i = 0; i < strings.size(); i++)
					{
						result = result + strings.get(i);
						if((i + 1) < strings.size())
							result = result + "$";
					}
					
					// create header and attach payload
					byte[] p = new byte[HEADER_SIZE + result.getBytes().length];
					
					ByteBuffer bb = ByteBuffer.wrap(p);
					bb.position(0);
					bb.putInt(PacketType.DATA.ordinal());
					Log.d("SENDEN", "Size " + result.getBytes().length);
					//bb.putInt(result.getBytes().length);
					bb.put(result.getBytes());
					bb.position(0);
					bb.get(p);
					
					Log.d(LOGTAG, result);
					try {
						if(sendMessage(p, srcEndpoint, SettingsData.default_data_ttl))
							Log.d(LOGTAG, "Message sent!");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				break;
				
			case DATA:
				Log.d(LOGTAG, "processIncomingMessage: DATA");
				String data = new String(payload);
				// split string into parts separated by '$'
				String[] parts = data.split( "\\$" );

		        //create list for this object array.
		        List<String> list = Arrays.asList(parts);

		        // create vector for given list
		        Vector<String> vector = new Vector<String>(list);
		        mLocationDatabase.insertLocations(vector);
		        for(int i = 0; i < vector.size(); i++)
		        	Log.d(LOGTAG, vector.get(i));
		        // parse vector to db
				break;
			}
		}

    	/**
    	 * Callback for the use of a third file descriptor.
    	 */
		@Override
		public ParcelFileDescriptor fd() {
			return null;
		}
		
		@Override
		public void progress(long current, long length) {
		}

		@Override
		public void finished(int startId) {
		}
		
   };
   
	/**
	 * Method for sending DTN bundles.
	 * @param message message to be sent
	 * @param endpoint destination DTN address
	 * @param time time to live
	 */
	public boolean sendMessage(byte[] message, String endpoint, int time) throws Exception
	{
		Session s = mClient.getSession();
		SingletonEndpoint destination = new SingletonEndpoint(endpoint);
		return s.send(destination, time, new String(message));
	}
   
	/**
	 * Class for the presence notification loop.
	 */
	public class PresenceTimerTask extends TimerTask
	{

    	/**
    	 * Method for sending presence messages.
    	 */
		@Override
		public void run() {
			byte[] payload = new byte[HEADER_SIZE];
			ByteBuffer bb = ByteBuffer.wrap(payload);
			bb.position(0);
			bb.putInt(PacketType.PRESENCE.ordinal());
			//bb.putInt(0);	// no payload
			bb.position(0);
			bb.get(payload);
			
			try {
				if(!sendMessage(payload, PRESENCE_GROUP_ID.toString(), SettingsData.default_presence_ttl))
					Log.d(LOGTAG, "Could not send presence notification!");
				Log.d(LOGTAG, "Presence notification sent!" + PRESENCE_GROUP_ID.toString() + " TTL: " + SettingsData.default_presence_ttl);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d(LOGTAG, "PRESENCE send exception");
				e.printStackTrace();
			}
			// TODO Auto-generated method stub
			
		}
	};
}
