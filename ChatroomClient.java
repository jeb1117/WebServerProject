/**
 * ChatroomClient.java
 * Client for Chat room (same setup of handler.java)
 * "What's the user" Protocol from CMPT 352
 * @author Kelsey Henrichsen and Jasmine Boonyakiti
 **/


import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.net.*;
import java.awt.Color;
import java.io.*;
import org.json.simple.*;
import java.util.*;

public class ChatroomClient implements Runnable
{
	private String userName = null;
	private Socket server;
	public JSONObject user = new JSONObject();
	StringBuffer userNameGen;
	Vector<String> group;
	JList readers;
	BufferedReader read = null;
	PrintWriter write = null;
	JTextPane inputs;
	Color styleThis;

	//constructor for styling the chatroom
	public void chatRoomStyle(JTextPane inputs, String colorThis, Color color){
		StyledDocument chatRoom = inputs.getStyledDocument();
		Style text = inputs.addStyle("Color", null);
		StyleConstants.setForeground(text, styleThis);
		try 
		{
			chatRoom.insertString(chatRoom.getLength(), colorThis, text);
		}
		catch(BadLocationException e)
		{
			e.printStackTrace();
		}
	}

	public void addClient(Socket server, String userName, JTextPane inputs) throws IOException
	{
		
		try
		{
			//read users input 
			read = new BufferedReader(new InputStreamReader(server.getInputStream()));

			while(true)
			{
				//checks for if the buffered reader is ready to be read
				if(read.ready())
				{
					//reads the line of the string
					String userInput = read.readLine();
					//prints the string itself
					System.out.println("finished reading" + userInput.toString());
					//creates a JSONValue cast to a JSONObject that will be used to parse the string value
					JSONObject readString = new JSONObject((JSONObject)JSONValue.parse(userInput));
					//checks if the json object equals chatroom-broadcast
					if(user.get("type").toString().equals("chatroom-broadcast"))
					{
						//creates a json array to keep track of who the messages is going to.
						JSONArray whoTo = (JSONArray)user.get("to");
						String clients = "";
						if(!whoTo.isEmpty())
						{
							//sets the message color as black
							chatRoomStyle(inputs, user.get("from").toString() + ": " + user.get("message").toString() + "\n", Color.BLACK);
						}
						else {
							//for loop to check if i is less than the "from" then send it to the "to"
							for(int i = 0; i < whoTo.size(); i++)
							{
								if(i == 0)
								{
									//used to combined the index of i and convert it to a string
									clients = clients.concat(whoTo.get(i).toString() + " ");
								}

								else if(i == whoTo.size() - 1)
								{
									//creates the and for the final user who enters last.
									clients = clients.concat("and" + (whoTo.get(i)));
								}
								else 
								{
									//separates users by having a comma on each section
									clients = clients.concat(whoTo.get(i) + ",");
								}
							}
							//sets the from user as yellow
							chatRoomStyle(inputs, "from " + user.get("from").toString() + " to " + clients + ": "  + user.get("message").toString() + "\n", Color.YELLOW);
						}
						//prints out the string message
						System.out.println(user.get("message").toString());
					}
					//checks if the user is entering or leaving.
					else if(user.get("type").toString().equals("chatroom-update"))
					{
						if(user.get("type_of_update").equals("enter"))
						{
							//gets the user id for each user
							group.remove(user.get("id"));
							//sets the id to a JList
							readers.setListData(group);
							chatRoomStyle(inputs, "Client " + user.get("id") + " has joined the chatroom.\n", Color.RED);


						}
						if(user.get("type_of_update").equals("leave"))
						{
							//gets the user id for each user
							group.remove(user.get("id"));
							//sets the id to a JList
							readers.setListData(group);
							chatRoomStyle(inputs, "Client " + user.get("id") + " has left the chatroom.\n", Color.RED);

						}


					}
					//checks if
					else if(user.get("type").toString().equals("chatroom-response")){ 
						if(((Long)user.get("id")).intValue() == -1){

							chatRoomStyle(inputs, "The Server is full at the moment, sucks to be you \n ", Color.ORANGE);

						}
						else{
							Long id = (Long)user.get("id");
							//appends the int value of the string id. i.e 1, 2, 3, etc. 
							userNameGen.append(":" + Integer.toString(id.intValue()));
							//prints out the string
							System.out.println(userNameGen.toString());
							System.out.println("int: " + Integer.toString(id.intValue()));
							//casts a json array to the json object so that it can be able to get the users.
							JSONArray people = (JSONArray)user.get("users");
							for(int i = 0; i < people.size(); i++){
								group.add((String)people.get(i));

							}
						}
					}
				}
			}
		}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				finally 
				{
					//closes the buffered reader when finished reading the line
					read.close();
				}

			}

			public ChatroomClient(Socket server, String userName, JTextPane inputs)
			{
				this.server = server;
				this.userName = userName;
				this.inputs = inputs;
			}

			public void run()
			{
				try
				{
					addClient(server, userName, inputs);
				}
				catch (java.io.IOException ioe) {
					System.err.println(ioe);
				}
			}




		}