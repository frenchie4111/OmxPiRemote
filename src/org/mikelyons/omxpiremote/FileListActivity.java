package org.mikelyons.omxpiremote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FileListActivity extends Activity {

	ListView listview;
	
	private SharedPreferences prefs;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_filelist);
	    
	    Toast toast = Toast.makeText(this, "Loading File List", Toast.LENGTH_LONG);
	    toast.show();
	    
	    listview = (ListView) findViewById(R.id.fileListview);
	    
	    ArrayList<String> values = new ArrayList<String>();
	    
	    prefs = getSharedPreferences("omxpi", Context.MODE_PRIVATE);
	    
	    
	    String url = prefs.getString("url", "");
	    String user = prefs.getString("user", "");
	    String pass = prefs.getString("pass", "");
	    
	    if( url.equals("") || user.equals("") || pass.equals("") ) {
	    	finish();
	    }
	    
	    String path = prefs.getString("path", "~/");
	    
	    String reply = "Failed";
	    try {
			reply = new SSHHandler().execute(url, user, pass, "ls -a "+ path + "").get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    if( !reply.equals("Failed") ) {
	    	values = new ArrayList<String>( SSHHandler.last_output );
	    	if( values.size() == 0 ) {
	    		values.add("No Files Found");
	    	}
	    } else {
	    	values.add("File Listing Failed");
	    }
	    
	    final FileListArrayAdapter adapter = new FileListArrayAdapter(this, android.R.layout.simple_list_item_1, values);
	    
	    
	    listview.setAdapter(adapter);
	    
	    toast.cancel();
	    
	    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    	@Override
	        public void onItemClick(AdapterView<?> parent, final View view,
	            int position, long id) {
	        		Toast.makeText(MainActivity.c, "Premium Feature\nBuy the full version", Toast.LENGTH_SHORT).show();
	        }
        });
	}
	
	private class FileListArrayAdapter extends ArrayAdapter<String> {
		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
		
		public FileListArrayAdapter( Context context, int textViewResourceId, List<String> objects) {
			super( context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}
		
		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}
		
		@Override
		public boolean hasStableIds() {
			return true;
		}
	}

}