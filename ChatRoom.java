import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatRoom
{
  static Scanner write = new Scanner(System.in);
  public static final int DEFAULT_PORT = 8029;
  public static void main(String[] args) throws IOException, UnknownHostException{

      Socket sock = new Socket(DEFAULT_PORT);
      DataInputStream chat = new DataInputStream(sock.getInputStream());
      DataOutputStream ret = new DataOutputStream(sock.getOutputStream());

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


  }
}
