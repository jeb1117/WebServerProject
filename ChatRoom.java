import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ChatRoom
{
  static Scanner write = new Scanner(System.in);
  public static final int DEFAULT_PORT = 8029;
  private static ArrayList<Socket> clientChat;
  ServerSocket sock = new ServerSocket(DEFAULT_PORT);
  DataInputStream chat;
  DataOutputStream ret;
  public ChatRoom() throws IOException
  {
	sock = new ServerSocket(3);
	clientChat = new ArrayList<Socket>();
	System.out.print("Waiting for connections...");
	
	
  }
  
  public void addClient(Socket client) throws IOException
  {
	  client = sock.accept();
	  clientChat.add(client);
	  System.out.println("A new person has joined");
	  
  }
  
  public void checkIn()
  {
	  
  }


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
              String msg = chat.readUTF();
              System.out.println(msg);
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


      public static void main(String[] args) throws IOException{
    	  ChatRoom server = new ChatRoom();
      }
}
