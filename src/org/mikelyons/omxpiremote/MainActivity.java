package org.mikelyons.omxpiremote;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	Button playpause;
	Button startServer;
	Button stopServer;
	
	EditText path;
	
	// TODO Get info from user rather than static
	// TODO Sanitize input
	public static String url = "lyons-pi.student.rit.edu";
	public static String user = "pi";
	public static String pass = "michael94";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Buttons Instantiate
        playpause = (Button) findViewById(R.id.playpause);
        startServer = (Button) findViewById(R.id.startServer);
        stopServer = (Button) findViewById(R.id.stopServer);
        
        // EditText Instantiate
        path = (EditText) findViewById(R.id.path);
        
        addListeners();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void addListeners() {    	
    	playpause.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.d("General", "Play Pause Clicked!");
			
				new SSHHandler().execute(url, user, pass, "echo -n 'p' >> /var/tmp/omx");
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
				new SSHHandler().execute(url, user, pass, "omxplayer --adev local " + location + " < /var/tmp/omx &");
				new SSHHandler().execute(url, user, pass, "echo '.' >> /var/tmp/omx");
			}
    	});
    	
    	stopServer.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Log.d("General", "Stop server Clicked!");
				new SSHHandler().execute(url, user, pass, "echo -n 'q' >> /var/tmp/omx");
			}
    	});
    }
    
}
