
package nptestapp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class ClientApp {
   private Socket server;
    
    /** Creates a new instance of ClientApp */
    public ClientApp()
    {
        // Attempt to establish connection to server
        try
        {
            // Create socket
            System.out.println("Attempting to connect to server...");
            server = new Socket("127.0.0.1", 6666); 
            System.out.println("Client connected to local server");
        }                    
        catch (IOException ioe)
        {
            System.out.println("IOException: " + ioe.getMessage());
        }
    }
    
    public void communicate()
    {
        // The connection has been established - now send/receive.
        
        try
        {        
            // Step 1: create channels
            System.out.println("Creating input and output streams on Client");
            ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(server.getInputStream());
            
            // Step 2: communicate
            System.out.println("Writing object to server");           
            out.writeObject("Radford");
            out.flush();
            System.out.println("Reading response from server");           
            String response = (String)in.readObject();
            System.out.println("From SERVER>> " + response);
            
            // Step 3: close down
            out.close();
            in.close();
            server.close();        
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
        ClientApp client = new ClientApp();
        client.communicate();
    }     
}// end of class
