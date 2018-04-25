/**
* ChatServer.java
* Server that runs on port 8029, following
* "What's the Dealio" Protocol from CMPT 352
* @author Kelsey Henrichsen and Jasmine Boonyakiti
**/
import java.net.*;
import java.util.*;
import java.io.*;
import org.json.simple.JSONObject;
import java.util.Vector; //most likely based on Cole's explanation
import java.util.HashMap; //most likely based on Cole's explanation

public class ChatServer
{
    public static final int PORT = 8029;
    public static final int maxClients; //declare how many l8r

    // create thread pool --> https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Executors.html
    private static final Executor executor = Executors.newCachedThreadPool();



    public static void main(String[] args) throws java.io.IOException {

        // create a server socket listening to port 8029
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Waiting for connections ....");
        
        // run & execute chatroom

        try
        {
            // makes sure that we are still within the chatroom client limits
            for(int i=0; i < maxClients; i++)
            {
                //add to clients until max reached
            }

            while (true) 
            { 
                // infinite loop in order to listen for new clients
                Socket server = server.accept();

                // add new client socket connection
                // using runnable and execute
                
            }
        }

        catch (IOException ioe)
        {
            System.out.println("IO Exception Thrown");
        }

        finally
        {
            // close the socket
            server.close();
        }
    }
}
