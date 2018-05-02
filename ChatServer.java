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
import java.util.Vector; 
import java.util.concurrent.*;


public class ChatServer
{
    public static final int PORT = 8029;
    public static final int maxClients = 5; //can change at any time

    // create thread pool --> https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/Executors.html
    private static final Executor executor = Executors.newCachedThreadPool();



    public static void main(String[] args) throws java.io.IOException {

        // create a server socket listening to port 8029
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("Waiting for connections ....");
        Vector<JSONObject> message = new Vector<JSONObject>();
        Vector<Integer> idCount = new Vector<Integer>();

        
        
        // run & execute chatroom
            // makes sure that we are still within the chatroom client limits
        ConcurrentHashMap<String, Socket> userName = new ConcurrentHashMap<String, Socket>();
        
        // run & execute chat room
        try
        {
            // makes sure that we are still within the chat room client limits
            for(int i=0; i < maxClients; i++)
            {
            	idCount.add(i);
            }

            while (true) 
            { 
                // infinite loop in order to listen for new clients
                // add new client socket connection
                // using runnable and execute
                // create thread, add new userName
            	
            	Runnable thread  = new Connection(server.accept(), userName,  message, idCount);
                executor.execute(thread);

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
