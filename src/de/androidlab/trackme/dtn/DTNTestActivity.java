package de.androidlab.trackme.dtn;

import android.app.Activity;
import de.androidlab.trackme.dtn.LocalDTNClient;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DTNTestActivity extends Activity implements OnClickListener {
	Button testButton;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        testButton = (Button)findViewById(R.id.button1);
        testButton.setOnClickListener(this);
        mClient = new LocalDTNClient();
        mClient.init(getApplicationContext(), "de.androidlab.trackme.dtn");
        //mClient.close(getApplicationContext());
    }
    
    LocalDTNClient mClient;
	@Override
	public void onClick(View v) {
		String test = "This is a testmessage!";
		try {
			mClient.sendMessage(test.getBytes(), "dtn://trackme.dtn/presence");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}