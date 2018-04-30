import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;

import org.json.simple.*;

public class Handler
{
	static Socket sock;
	DataInputStream chat;
	DataOutputStream ret;
	static Scanner write = new Scanner(System.in);
	public JSONObject startChat = new JSONObject();


	@SuppressWarnings("unchecked")
	public void addClient(Socket client, ConcurrentHashMap<String, Socket> userName, Vector<JSONObject> message, Vector<Integer> idCount) throws IOException
	{
		if(idCount.lastElement() < 20 && userName.size() < 20){

			startChat.put("type", "chatroom-response");
			// there is room for more clients
			if(idCount.isEmpty()) {

				startChat.put("id", idCount); //idk why this is throwing an error
				startChat.put("clientNo", userName.size());
				startChat.put("userName", userName.keySet().toArray());
				System.out.println("A new person has joined");
			}
			// amount of clients allowed is full
			else {
				startChat.put("id", new Integer(-1));
				startChat.put("clientNo", userName.size());
				startChat.put("userName", userName.keySet().toArray());
			}
		}

		// user name too long
		else
		{
			startChat.put("type", "chatroom-error");
			String [] errorType = {"user_name_length_exceeded"};
			startChat.put("type_of_error", errorType);

		}

	}

	public void broadCast(Socket client, ConcurrentHashMap<String, Socket> userName, Vector<JSONObject> message, Vector<Integer> idCount, JSONObject sendMess) throws IOException
	{
		JSONObject broadcast = new JSONObject();
		try {
			ret = new DataOutputStream(client.getOutputStream());
			while(true) 
			{
				broadcast.put("type", "chatroom-broadcast");
				broadcast.put("from", userName);
				broadcast.put("to", "[]");
				broadcast.put("message", sendMess);
				broadcast.put("len", ret.size());
				try
				{
					Thread.sleep(1000);
				}
				catch(InterruptedException ioe)
				{ }
				

			}
		}
		catch(IOException ioe)
		{
			System.out.println(ioe);
		}
		finally 
		{
			if(chat != null)
			{
				chat.close();
			}
		}

	}


	@SuppressWarnings("unchecked")
	public void sendMsg(Vector<Integer> idCount) {

		int end = idCount.remove(0);
		JSONObject sendMess = new JSONObject();
		Thread sendIt = new Thread(new Runnable()
		{

			public void run()
			{

				String msg = write.nextLine();
				try
				{
					if(ret.size() < 280) {
						ret.writeUTF(msg);
						sendMess.put("message", ret);
					}
					else {
						sendMess.put("type", "chatroom-error");
						String[] errorType = {"message_exceeded_max_length"};
						sendMess.put("type_of_error", errorType);
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		});

		Thread readIt = new Thread(new Runnable()
		{

			public void run()
			{
				while(true)
				{
					try
					{
						String msg = "";
						if(!msg.equals("exit"))
						{
							msg = chat.readUTF();
							System.out.println(msg);
						}
						else
						{
							sendMess.put("type", "chatroom-end");
							sendMess.put("id", end);
							sendMess.put("type", "chatroom-update");
							sendMess.put("type_of_update", idCount.size()); //not sure about this
							sendMess.put("id", end);
						}
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			}
		});

		sendIt.start();
		readIt.start();
	}


}
