package prac2a;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GuiServerApp extends JFrame implements ActionListener {

    private ServerSocket listener;
    private String msg = "";
    private String upCaseMsg = "";
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private JButton exitBtn = new JButton("EXIT");
    private JTextArea clientTxtArea = new JTextArea(5, 40);
    private String response = "";
    private JPanel topPanel = new JPanel();
    private JPanel centerPanel = new JPanel();

    // Client connection
    private Socket client;

    // Define a constructor in which you construct a ServerSocket object and setup the Gui
    public GuiServerApp() {
        super("Gui Server App");

        topPanel.add(exitBtn);
        exitBtn.addActionListener(this);
        centerPanel.add(new JScrollPane(clientTxtArea));

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        // Create server socket
        try {
            listener = new ServerSocket(6666); // the 2nd parameter is the maximum number of connections
        } catch (IOException ioe) {
            System.out.println("IO Exception: " + ioe.getMessage());
        }
    }

    //declare a method to listen for client connections
    private void listenForClients() throws IOException {
        try {
            client = listener.accept();
            clientTxtArea.append("Client connection has been accepted\n");
            createStreams();
        } catch (IOException ioe) {
            clientTxtArea.append("IO Exception in Client sendData(): " + ioe.getMessage());
        }
    }

    //declare a method to initiate communication streams
    private void createStreams() throws IOException {
        try {
            out = new ObjectOutputStream(client.getOutputStream());
            out.flush();
            in = new ObjectInputStream(client.getInputStream());

        } catch (IOException ioe) {
            clientTxtArea.append("IO Exception in Client sendData(): " + ioe.getMessage());
        }
    }

    //declare a method in which you write data to the client    
    private void sendData(String msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException ioe) {
            clientTxtArea.append("IO Exception in Client sendData(): " + ioe.getMessage());
        }

    }

    //declare a method in which you close the streams and the socket connection 
    public void closeConnection() {
        try {
            out.close();
            in.close();
            client.close();
            clientTxtArea.append("Server now closing down\n");
            clientTxtArea.append("CLICK ON BUTTON TO EXIT");
        } catch (IOException ioe) {
            System.out.println("IO Exception: " + ioe.getMessage());
        }
    }

    //declare a method in which you continuously read from the client; process the incoming data; and write results back to client.    
    public void processClient() throws IOException {
        try {
            do {
                // read from the client.
                msg = (String) in.readObject();
                clientTxtArea.append("From CLIENT >> " + msg + "\n");
                if (msg.equalsIgnoreCase("Exit")) {
                    // Do nothing - just exit the loop
                    break;

                } else if (msg.equalsIgnoreCase("Terminate")) {
                    // Convert Terminate to uppercase and send it
                    upCaseMsg = msg.toUpperCase();
                    sendData(upCaseMsg);

                    // Close connection
                    clientTxtArea.append("Client has logged off\n");
                    closeConnection();
                } else {
                    // Convert all other messages to uppercase
                    upCaseMsg = msg.toUpperCase();
                    sendData(upCaseMsg);
                }
            } while (!msg.equalsIgnoreCase("Exit"));

        } catch (ClassNotFoundException ioe) {
            System.out.println("Class not found: " + ioe.getMessage());
        }
    }

    public static void main(String[] args) {
        // Create application
        GuiServerApp server = new GuiServerApp();

        server.setSize(500, 300);
        server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        server.setVisible(true);

        try {
            server.listenForClients();
            server.processClient();
        } catch (IOException ioe) {
            System.out.println("Error: " + ioe.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitBtn) {
            System.exit(0);
        }
    }

}// end of class
