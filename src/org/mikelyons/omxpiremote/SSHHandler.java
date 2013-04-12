package org.mikelyons.omxpiremote;

import java.io.IOException;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import android.os.AsyncTask;
import android.util.Log;

public class SSHHandler extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... url) {
		Log.v("SSH Thread", "command" + url[0]);
		
		if(url[0] == "omx") {
			
		}
		
		JSch jsch = new JSch();
		
		try {
			Session session = jsch.getSession("pi", "lyons-pi.student.rit.edu");
			session.setPassword("michael94");
			
			java.util.Properties config = new java.util.Properties(); 
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			
			session.connect(30000);
			
			Log.v("Console", session.isConnected()?"True":"False" );
			
			Channel channel = session.openChannel("exec");
			
			((ChannelExec)channel).setCommand("echo '.' >> /var/tmp/omx");
			
			channel.connect();	
			
			channel.disconnect();
		    session.disconnect();
			
		} catch (JSchException e) {
			e.printStackTrace();
		}
		return "Sent";
	}
	
}
