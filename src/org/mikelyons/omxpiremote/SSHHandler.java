package org.mikelyons.omxpiremote;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import android.os.AsyncTask;
import android.util.Log;

public class SSHHandler extends AsyncTask<String, Void, Boolean> {

	@Override
	protected Boolean doInBackground(String... args) {
		
		String url = args[0];
		String user = args[1];
		String pass = args[2];
		String command = args[3];		
		
		Log.v("SSH Thread", "Url: " + url);
		Log.v("SSH Thread", "User: " + user);
		Log.v("SSH Thread", "Pass: " + pass);
		Log.v("SSH Thread", "Command: " + command);
		
		JSch jsch = new JSch();
		
		try {
			// Set up session
			Session session = jsch.getSession(user, url);
			session.setPassword(pass);
			
			// Turn off SSH HostKey check (Easier than dealing with hostkey)
			java.util.Properties config = new java.util.Properties(); 
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			
			// Connect to server
			session.connect(30000);
			
			// Check if connected TODO Fail on not-connected
			Log.v("Console", session.isConnected()?"True":"False" );
			
			// Create channel for sending commands
			Channel channel = session.openChannel("exec");
			
			// Set command to send
			((ChannelExec)channel).setCommand(command);
			
			// Send command
			channel.connect();	
			
			// Disconnect !important
			channel.disconnect();
		    session.disconnect();
			
		} catch (JSchException e) {
			e.printStackTrace();
		}
		return true; // TODO Return if command sent successfully
	}
	
}
