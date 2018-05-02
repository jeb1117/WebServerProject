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
	PrintWriter write = null;

	
	public void addClient(Socket server, String userName) throws IOException
	{
		try
		{
		//read users input about user name
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String userInput;
		System.out.println("Please enter your desired username; must be less 20 character");
		userInput = reader.readLine();
		
		//create user name with JSON
		user.put("type", "chatroom-begin");
		user.put("id", userInput);
		user.put("len", userInput.length());
		userName = userInput; //save's a user name to a variable
		
		write = new PrintWriter("ChatRoom.json");
		write.write(user.toString());
		
		while(true)
		{
			userInput = reader.readLine(); // reads message from client 
			chat.put("type", "chatroom-send");
	        chat.put("from", userName);
	        chat.put("message", userInput);
	        chat.put("to", "[]");
	        chat.put("message-length", userInput.length());
			
		}
		}
		catch(IOException ioe){}
		finally
		{
			server.close();
			write.close();
			
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