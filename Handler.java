import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;

import org.json.simple.*;

import com.sun.xml.internal.ws.api.message.Messages;

public class Handler
{
	static Socket sock;
	public JSONObject startChat = new JSONObject();
	public JSONObject continueChat = new JSONObject();
	public JSONObject broadcast = new JSONObject();
	BufferedReader read = null;
	PrintWriter writer = null;




	public void addClient(Socket client, ConcurrentHashMap<String, Socket> userName, Vector<JSONObject> message, Vector<Integer> idCount) throws IOException
	{
		Iterator iterate = null;
		try {

			String mess = read.readLine();
			//parse the request from dealio 
			JSONObject chats = new JSONObject((JSONObject)JSONValue.parse(mess));
			Long start = (Long) chats.get("len");

			//check to see if user name is too long (20 is max) 
			if(start.intValue() < 20){

				//begin dealio for chatroom-response
				startChat.put("type", "chatroom-response");

				// there is room for more clients
				if(!idCount.isEmpty()) 
				{

					startChat.put("id", idCount);
					startChat.put("clientNo", userName.size());
					startChat.put("userName", userName.keySet().toArray());

					//combine username and id in system for one chunk
					String userID = startChat.get("userName").toString() + " " + idCount;

					// dealio for chatroom-update -- for every new user
					startChat.put("type", "chatroom-update");
					startChat.put("type_of_update", "enter");
					startChat.put("id", userID);
					message.add(startChat);

				}

				// amount of clients allowed is full -- finishes up dealio
				else {

					startChat.put("id", new Integer(-1));
					startChat.put("clientNo", userName.size());
					startChat.put("userName", userName.keySet().toArray());

				}
			}

			// user name too long (greater than 20) -- throw chatroom-error dealio 
			else
			{

				startChat.put("type", "chatroom-error");
				String[] error = {"user_name_length_exceeded"};
				startChat.put("type_of_error" , error); 

			}

			writer = new PrintWriter(client.getOutputStream(), true);
			writer.println(startChat.toString());

			while(true)
			{
				if(read.ready())
				{

					String anotherDealio = read.readLine();
					JSONObject continueChat = new JSONObject((JSONObject)JSONValue.parse(anotherDealio));
					String request = continueChat.get("type").toString();

					if(request.equals("chatroom-send"))
					{

						while(!message.isEmpty())
						{

							JSONObject removedMessage = message.remove(0);
							System.out.println(removedMessage);
							iterate = userName.entrySet().iterator();
							Long remove = (Long) removedMessage.get("len");
								
							if(remove < 280)
							{

								Map.Entry getNext = (Map.Entry)iterate.next();

								String toMess = (String) removedMessage.get("to");
								String whoFrom = (String) removedMessage.get("from").toString();

								if(removedMessage.get("type").toString().equals("chatroom-update"))
								{
									while(iterate.hasNext())
									{

										Socket next = (Socket) getNext.getValue();
										System.out.println(getNext.getValue());
										writer = new PrintWriter(next.getOutputStream(), true);
										writer.println(message.toString());

									}
								}
								else if(removedMessage.get("type").toString().equals("chatroom-broadcast"))
								{
									System.out.println("Broadcasting to Users");
									while (iterate.hasNext()) {
										if(toMess.equals("[]"))
										{
											Socket next = (Socket) getNext.getValue();
											System.out.println(getNext.getValue());
											writer = new PrintWriter(((Socket)getNext.getValue()).getOutputStream(), true);
											writer.println(message.toString());

										}
										if(whoFrom.equals(getNext.getKey())){
							                writer = new PrintWriter(((Socket)getNext.getValue()).getOutputStream(), true);
							                writer.println(message.toString());
							              }

									}
								}
								//uses broadcast to cast
								else if(removedMessage.get("type").toString().equals("chatroom-send"))
								{
									broadCast(removedMessage, message);
								}
								//chatroom-special, since it is not implemented there will be the chatroom-error. 
								else if(removedMessage.get("type").toString().equals("chatroom-special"))
								{
									removedMessage.put("type", "chatroom-error");
									removedMessage.put("type_of_error", "special_unsupported");
								}
							}

							//message is too long (greater than 280), throw chatroom-error dealio
							else
							{

								continueChat.put("type", "chatroom-error");
								String [] error = {"message_exceeded_max_length"};
								continueChat.put("id", error);

							}
							try 
							{ 
								Thread.sleep(1000); 
							} 
							catch (InterruptedException ignore) { }
						}

					}
				}
			}
		}
		catch(IOException e){
			System.out.println(e);
		}
		finally
		{
			read.close();
			writer.close();
			client.close();

		}

	}

	private static void broadCast(JSONObject json, Vector<JSONObject> message) throws IOException
	{
		PrintWriter writer = null;
		Iterator iterate = null;
		JSONObject removedMessage = message.remove(0);
		String whoFrom = (String) json.get("from");
		String whoTo = (String) json.get("to");
		String mess =  (String) json.get("message");


		int length = ((Long)json.get("len")).intValue();
		JSONObject broadcast = new JSONObject();
		broadcast.put("type", "chatroom-broadcast");
		broadcast.put("from", whoFrom);
		broadcast.put("to", whoTo);
		broadcast.put("message", mess);
		broadcast.put("len", length);

		message.add(broadcast);


	}

}



