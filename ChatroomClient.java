/**
 * ChatroomClient.java
 * Client for Chat room (same setup of handler.java)
 * "What's the Dealio" Protocol from CMPT 352
 * @author Kelsey Henrichsen and Jasmine Boonyakiti
 **/

import java.util.concurrent.ConcurrentHashMap;
import java.net.*;
import java.io.*;
import org.json.simple.*;
import java.util.*;

public class ChatroomClient
{
	private String userName = null;
	private Socket server;
	public JSONObject user = new JSONObject();
	public JSONObject chat = new JSONObject();
	
	public void addClient(Socket client, String users) throws IOException
	{
		//read users input about user name
		BufferedReader reader = null;
		String userInput;
		System.out.println("Please enter your desired username");
		userInput = reader.readLine();
		
		//if statement checking length of user name then:
		//create user name with JSON
		user.put("type", "chatroom-begin");
		user.put("id", userInput);
		user.put("len", userInput.length());
		userName = userInput;
		
		while(true)
		{
			userInput = reader.readLine();
			chat.put("type", "chatroom-send");
	        chat.put("from", userName);
	        chat.put("message", userInput);
	        chat.put("to", new String[0]);
	        chat.put("message-length", userInput.length());
			
		}
	}
	
	public ChatroomClient(Socket server, String userName)
	{
		this.server = server;
		this.userName = userName;
	}
	
	public void run()
	{
		try
		{
			addClient(server, userName);
		}
		catch (java.io.IOException ioe) {
		      System.err.println(ioe);
		    }
	}

}