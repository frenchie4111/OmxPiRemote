package org.mikelyons.omxpiremote;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	Button button;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        addListeners();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void addListeners() {
    	button = (Button) findViewById(R.id.button2);
    	
    	button.setOnClickListener( new OnClickListener() {
    			@Override
    			public void onClick(View arg0) {
    				Log.d("General", "Button Clicked!");
    				
    				new SSHHandler().execute("echo '.' >> /var/tmp/omx");
    			}
    	});
    }
    
}
