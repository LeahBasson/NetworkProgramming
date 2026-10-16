
package nptestapp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class ServerApp {
   // Server socket
    private ServerSocket listener;
    
    // Client connection
    private Socket client;
    
    /** Creates a new instance of ServerApp */
    public ServerApp()
    {
        // Create server socket
        try {
            listener = new ServerSocket(6666, 1); // the 2nd parameter is the maximum number of connections
        }
        catch (IOException ioe)
        {
          System.out.println("IO Exception: " + ioe.getMessage());
        }
    }
    
    public void listen()
    {
        // Start listening for client connections
        try {
          System.out.println("Server is listening");
          client = listener.accept();  
          System.out.println("Client connection has been accepted");
          
          processClient();
        }
        catch(IOException ioe)
        {
            System.out.println("IO Exception: " + ioe.getMessage());
        }
    }
    
    public void processClient()
    {
        // Communicate with the client
        
        // First step: initiate channels
        System.out.println("Creating input and output streams on Server");
        try {
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            
            // Step 2: communicate
            System.out.println("Reading request object from client");           
            String msg = (String)in.readObject();
            System.out.println("From CLIENT>> " + msg);
            System.out.println("Writing response to client");           
            out.writeObject("Hello " + msg);
            out.flush();
            
            // Step 3:close down
            out.close();
            in.close();
            client.close();        
        }
        catch (IOException ioe)
        {
            System.out.println("IO Exception: " + ioe.getMessage());
        }
        catch (ClassNotFoundException cnfe)
        {
            System.out.println("Class not found: " + cnfe.getMessage());
        }
    }
  
    public static void main(String[] args)
    {
        // Create application
        ServerApp server = new ServerApp();
        
        // Start waiting for connections
        server.listen();
    }     
}// end of class
