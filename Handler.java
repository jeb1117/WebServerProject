import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
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


	public void addClient(Socket client, HashMap<String, Socket> users) throws IOException
	{

		if() 
		{
			if(idCount == null) {
				startChat.put("id", idCount.incrementAndGet());
				startChat.put("clientNo", users.size());
				startChat.put("users", users.keySet().toArray());
				System.out.println("A new person has joined");
			}
			else {
				startChat.put("id", new Integer(-1));
				startChat.put("clientNo", users.size());
				startChat.put("users", users.keySet().toArray());
			}
		}

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
		ChatRoom server = new ChatRoom();
	}

}
