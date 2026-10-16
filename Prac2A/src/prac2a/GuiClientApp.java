package prac2a;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import javax.swing.*;

public class GuiClientApp extends JFrame implements ActionListener {

    private Socket server;
    private JButton exitBtn = new JButton("EXIT");
    private JLabel clientLbl = new JLabel("Enter text here");
    private JTextField clientTxt = new JTextField(20);
    private JTextArea serverTxt = new JTextArea(5, 40);
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String response = "";
    private JPanel topPanel = new JPanel();
    private JPanel bottomPanel = new JPanel();

    public GuiClientApp() {
        super("Client App");

        topPanel.add(exitBtn);
        topPanel.add(new JScrollPane(clientLbl));
        topPanel.add(clientTxt);
        bottomPanel.add(serverTxt);

        exitBtn.setVisible(false);

        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);
        clientTxt.addActionListener(this);
        exitBtn.addActionListener(this);

        try {
            // Create socket
            System.out.println("Attempting to connect to server...");
            server = new Socket("127.0.0.1", 6666);
            createStreams(); // send and receive objects.

            System.out.println("Client connected to local server");
        } catch (IOException ioe) {
            System.out.println("IOException: " + ioe.getMessage());
        }
    }

    //declare a method in which you setup the communication streams    
    private void createStreams() throws IOException {
        try {
            out = new ObjectOutputStream(server.getOutputStream());
            out.flush();
            in = new ObjectInputStream(server.getInputStream());
        } catch (IOException ioe) {
            serverTxt.append("IO Exception in Client createStreams(): " + ioe.getMessage());
        }
    }

    //declare a method in which you write data to the server    
    private void sendData(String clientMsg) {
        try {
            out.writeObject(clientMsg);
        } catch (IOException ioe) {
            serverTxt.append("IO Exception in Client sendData(): " + ioe.getMessage());
        }
    }

    //declare the actionPerformed method in which you send the required data to the server    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == clientTxt) {
            sendData(clientTxt.getText());
            clientTxt.setText("");
        } else {
            System.exit(0);
        }
        
        if (e.getSource() == exitBtn) {
            System.exit(0);
        }
    }

    //declare a method in which you continuously read from the server.
    public void communicate() throws ClassNotFoundException {
        /*Since this is a GUI-driven client application
             * it is only necessary to continously read from the server until the value 'terminate' is read.
             * Writing to the server takes place when the user clicks on the textfield. So the code to send data 
             * is in the actionPerformed method.             
         */
        try {
            do {
                response = (String) in.readObject();

                serverTxt.append("From SERVER>> " + response + "\n");

            } while (!response.equalsIgnoreCase("TERMINATE")
                    && !response.equalsIgnoreCase("EXIT"));

            // These happen AFTER the loop ends
            serverTxt.append("Closing connection to server\n");
            serverTxt.append("CLICK ON BUTTON TO EXIT\n");

            exitBtn.setVisible(true);

            closeConnection();

        } catch (IOException ioe) {
            serverTxt.append("IO Exception in Client communicate(): "
                    + ioe.getMessage());
        }
    }

    //declare a method in which you close the streams and the socket connection
    public void closeConnection() {
        try {
            out.close();
            in.close();
            server.close();
        } catch (IOException ioe) {
            System.out.println("IO Exception: " + ioe.getMessage());
        }
    }

    //declare a method in which you close the streams and the socket connection
    public static void main(String[] args) {

        GuiClientApp client = new GuiClientApp();

        client.setSize(500, 300);
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.setVisible(true);

        try {

            client.communicate();
        } catch (ClassNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

}// end of class
