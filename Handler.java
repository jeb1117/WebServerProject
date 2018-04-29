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
	private static AtomicLong idCount = new AtomicLong();


	public void addClient(Socket client, ConcurrentHashMap<String, Socket> userName, Vector<JSONObject> message, Vector<Integer> idCount) throws IOException
	{
		{
			// there is room for more clients
			if(idCount == null) {
				startChat.put("id", idCount.incrementAndGet()); //idk why this is throwing an error
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
//		else
//		{
//			// chatroom-error
//		}

	}

	public void sendMsg() {

		Thread sendIt = new Thread(new Runnable()
		{
			public void run()
			{

				String msg = write.nextLine();
				try
				{
					ret.writeUTF(msg);
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
	public static void main(String[] args) throws IOException{
		ChatServer server = new ChatServer();

	}

}
