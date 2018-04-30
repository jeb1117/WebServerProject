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
import java.util.concurrent.ConcurrentHashMap;
import java.util.Vector;
import org.json.simple.JSONObject;



public class Connection implements Runnable
{
	private Socket client;
	private ConcurrentHashMap<String, Socket> userName;
	private static Handler handler = new Handler();
	private Vector<JSONObject> message;
	private Vector<Integer> idCount;
	private JSONObject sendMess;

	public Connection(Socket client, ConcurrentHashMap<String, Socket> userName, Vector<JSONObject> message, Vector<Integer> idCount, JSONObject sendMess) {
		this.client = client;
		this.userName = userName;
		this.message = message;
		this.idCount = idCount;
		this.sendMess = sendMess;
	}

	public void run(){
		try {
		handler.addClient(client, userName, message, idCount);
		handler.broadCast(client, userName, message, idCount, sendMess);
		handler.sendMsg(idCount);
	}
	catch (java.io.IOException ioe) {
		System.err.println(ioe);
	}
	}
}