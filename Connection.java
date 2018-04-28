/**
 * This is the separate thread that services each
 * incoming echo client request.
 *
 * @author Greg Gagne
 */

import java.net.*;
import java.io.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import java.util.HashMap;


public class Connection implements Runnable
{
	private Socket client;
	private HashMap<String, Socket> users;
	private static Handler handler = new Handler();

	public Connection(Socket client, HashMap<String, Socket> users) {
		this.client = client;
		this.users = users;
	}

	public void run(){
		try {
		handler.addClient(client, users);
	}
	catch (java.io.IOException ioe) {
		System.err.println(ioe);
	}
	}
}