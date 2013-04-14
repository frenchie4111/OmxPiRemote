package org.mikelyons.omxpiremote;

import java.util.concurrent.ExecutionException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button playpause;
	Button startServer;
	Button stopServer;
	
	Button seekLeftSmall;
	Button seekLeftLarge;
	Button seekLeft;
	
	Button seekRightSmall;
	Button seekRightLarge;
	Button seekRight;
	
	Button volumeDown;
	Button volumeUp;
	
	EditText path;
	
	// TODO Get info from user rather than static
	// TODO Sanitize input
	public static String url = "lyons-pi.student.rit.edu";
	public static String user = "pi";
	public static String pass = "michael94";
	
	
	
	public static Context c;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Buttons Instantiate
        playpause = (Button) findViewById(R.id.playpause);
        startServer = (Button) findViewById(R.id.startServer);
        stopServer = (Button) findViewById(R.id.stopServer);
        
        seekLeftLarge = (Button) findViewById(R.id.seekLeftBig);
        seekLeftSmall = (Button) findViewById(R.id.seekLeftSmall);
        seekLeft = (Button) findViewById(R.id.seekLeft);
        
        seekRightLarge = (Button) findViewById(R.id.seekRightBig);
        seekRightSmall = (Button) findViewById(R.id.seekRightSmall);
        seekRight = (Button) findViewById(R.id.seekRight);
        
        volumeUp = (Button) findViewById(R.id.volumeUp);
        volumeDown = (Button) findViewById(R.id.volumeDown);
        
        // EditText Instantiate
        path = (EditText) findViewById(R.id.path);
        
        c = getApplicationContext();
        
        addListeners();
    }

    @Override
    public void onDestroy() {
    	SSHHandler.disconnect();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.settings:
    			Toast.makeText(c, "Settings Pressed", Toast.LENGTH_SHORT).show();
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
    	}
    }
    
    public void addListeners() {    	
    	playpause.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.d("General", "Play Pause Clicked!");
			
				runCommand("echo -n 'p' >> /var/tmp/omx");
			}
    	});
    	
    	startServer.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String location = "http://mikelyons.org/external/cos.mp3";
				Log.d("General", "Start server Clicked!");
				if( !path.getText().toString().matches("") ) {
					location = path.getText().toString();
				}
				runCommand("killall omxplayer.bin\nmmkfifo /var/tmp/omx 2> /dev/null\nomxplayer --adev local " + location + " < /var/tmp/omx &\necho '.' >> /var/tmp/omx");
			}
    	});
    	
    	stopServer.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.d("General", "Stop server Clicked!");
				runCommand("echo -n 'q' >> /var/tmp/omx\nkillall omxplayer.bin\nrm -rf /var/tmp/omx 2> /dev/null");
			}
    	});
    	
    	seekLeftLarge.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				runCommand("echo -n '\\x1b\\x5b\\x42' >> /var/tmp/omx");
			}
    	});
    	
    	volumeUp.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				runCommand("echo -n '+' >> /var/tmp/omx");
			}
    	});
    	
    	volumeDown.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				runCommand("echo -n '- m' >> /var/tmp/omx");
			}
    	});
    }
    
    public void runCommand(String cmd) {
    	String reply = "Failed";    	
    	try {
			reply = new SSHHandler().execute(url, user, pass, cmd).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if( !reply.equals(SSHHandler.SUCCESS) ) {
    		CharSequence text = reply;
    		Toast.makeText(c, text, Toast.LENGTH_SHORT).show();
    		
    	}
    }
    
}
