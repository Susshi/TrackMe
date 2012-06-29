package de.androidlab.trackme.dtn;

import java.util.Date;
import java.util.StringTokenizer;
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
import android.preference.PreferenceManager;
import android.util.Log;

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

public class LocalDTNClient {
	
	private final static String LOGTAG = "LocalDTNClient";
	private String mPackageName = getClass().getPackage().getName();
	public static final GroupEndpoint PRESENCE_GROUP_ID = new GroupEndpoint("dtn://trackme.dtn/presence");
	private Context mContext = null;
	
	// executor to process local job queue
	private ExecutorService mExecutor = null;
	
	// DTN client to talk with the DTN service
	private LDTNClient mClient = null;	
	
	protected class LDTNClient extends DTNClient {
		
		public LDTNClient() {
			super(mPackageName);
		}

		@Override
		protected void sessionConnected(Session session) {
			Log.d(LOGTAG, "DTN session connected");
			
	        // check for bundles first
	        mExecutor.execute(mQueryTask);
		}

		@Override
		protected CallbackMode sessionMode() {
			return CallbackMode.SIMPLE;
		}

		@Override
		protected void online() {
			Log.i(LOGTAG, "DTN is online.");
		}

		@Override
		protected void offline() {
			Log.i(LOGTAG, "DTN is offline.");
		}
		
	};
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(de.tubs.ibr.dtn.Intent.RECEIVE))
			{
				Log.d(LOGTAG, "Intent received!");
				// RECEIVE intent received, check for new bundles
				mExecutor.execute(mQueryTask);
			}
		}
	};
	
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
	
	public void init(Context context, String packageName) {
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
			Log.i("LocalDTNClient", "Client successful initialized");
		} catch (ServiceNotAvailableException e) {
			showInstallServiceDialog(context);
		}
        
		Log.d(LOGTAG, "DTN-Client created");
    }
    
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
	
	public void close(Context context)
	{
		// unregister intent receiver
		context.unregisterReceiver(mReceiver);
		
		// unregister at the daemon
		mClient.unregister();
		
		try {
			// stop executor
			mExecutor.shutdown();
			
			// ... and wait until all jobs are done
			if (!mExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
				mExecutor.shutdownNow();
			}
			
			// destroy DTN client
			mClient.terminate();
		} catch (InterruptedException e) {
			Log.e(LOGTAG, "Interrupted on service destruction.", e);
		}
		
		// clear all variables
		mExecutor = null;
		mClient = null;
	}

   private DataHandler mHandler = new DataHandler() {
    	Bundle current;

		@Override
		public void startBundle(Bundle bundle) {
			this.current = bundle;
		}

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

		@Override
		public void startBlock(Block block) {
		}

		@Override
		public void endBlock() {
		}

		@Override
		public void characters(String data) {
		}

		@Override

		public void payload(byte[] data) {
			String msg = new String(data);
			Log.i(LOGTAG, "New incoming message from" + current.source + ": " + msg);
			mExecutor.execute(new Runnable() {
		        public void run() {
					try {
						sendMessage(new String("This is the answer!").getBytes(), current.source);
					} catch (Exception e) {
						Log.e(LOGTAG, "Could not send message to endpoint!");
					}
		        }});
		        
			/*
			String answer = "This is a answer!";
			try {
				//sendMessage(answer.getBytes(), current.source);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			
			/* TODO
			if (current.destination.equalsIgnoreCase(PRESENCE_GROUP_EID.toString()))
			{
				eventNewPresence(current.source, current.timestamp, msg);
			}
			else
			{
				eventNewMessage(current.source, current.timestamp, msg);
			}
			*/
		}

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
    
		private void eventNewPresence(String source, Date created, String payload)
		{
			Log.i(LOGTAG, "Presence received from " + source);
			
			// buddy info
			String nickname = null;
			String presence = null;
			String status = null;
			
			StringTokenizer tokenizer = new StringTokenizer(payload, "\n");
			while (tokenizer.hasMoreTokens())
			{
				String data = tokenizer.nextToken();
				
				// search for the delimiter
				int delimiter = data.indexOf(':');
				
				// if the is no delimiter, ignore the line
				if (delimiter == -1) return;
				
				// split the keyword and data pair
				String keyword = data.substring(0, delimiter);
				String value = data.substring(delimiter + 1, data.length()).trim();
				
				if (keyword.equalsIgnoreCase("Presence"))
				{
					presence = value;
				}
				else if (keyword.equalsIgnoreCase("Nickname"))
				{
					nickname = value;
				}
				else if (keyword.equalsIgnoreCase("Status"))
				{
					status = value;
				}
			}
			
			if (nickname != null)
			{
			}
		}
		
		public void eventNewMessage(String source, Date created, String payload)
		{
			if (source == null)
			{
				Log.e(LOGTAG, "message source is null!");
			}
			
			// create a status bar notification
			Log.i(LOGTAG, "New message received!");
			}
	    };
	    
		public void sendMessage(byte[] message, String endpoint) throws Exception
		{
			Session s = mClient.getSession();
			SingletonEndpoint destination = new SingletonEndpoint(endpoint);
			String lifetime = PreferenceManager.getDefaultSharedPreferences(mContext).getString("messageduration", "259200");
			if (!s.send(destination, Integer.parseInt(lifetime), new String(message)))
			{
				throw new Exception("could not send the message");
			}
			else
				Log.i(LOGTAG, "Message send!");
		}
}
