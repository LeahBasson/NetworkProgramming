package prac2a;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Scanner;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class GuiServerApp extends JFrame implements ActionListener {

    private ServerSocket listener;
    private String msg = "";
    private String upCaseMsg = "";
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private JButton exitBtn = new JButton("EXIT");
    private JTextArea clientTxtArea = new JTextArea(5, 40);

    private JPanel topPanel = new JPanel();
    private JPanel centerPanel = new JPanel();

    // Client connection
    private Socket client;

    // Scanner for server input
    private Scanner keyboard = new Scanner(System.in);

    //----------------------------------------------------------------------------------
    // Constructor
    public GuiServerApp() {
        super("Server App");

        topPanel.add(exitBtn);
        centerPanel.add(new JScrollPane(clientTxtArea));

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        exitBtn.addActionListener(this);

        try {
            listener = new ServerSocket(6666, 1);
        } catch (IOException ioe) {
            System.out.println("IO Exception: " + ioe.getMessage());
        }
    }

    // Listen for client connections
    private void listenForClients() throws IOException {

        client = listener.accept();

        clientTxtArea.append("Client connected.\n");

        getStreams();
    }

    // Initiate communication streams
    private void getStreams() throws IOException {

        out = new ObjectOutputStream(client.getOutputStream());
        out.flush();

        in = new ObjectInputStream(client.getInputStream());
    }

    // Write data to the client
    private void sendData(String myMsg) {

        try {
            out.writeObject(myMsg);
            out.flush();

        } catch (IOException ioe) {
            clientTxtArea.append(
                    "IO Exception: " + ioe.getMessage() + "\n"
            );
        }
    }

    // Close streams and socket connection
    private void closeConnection() {

        try {

            if (out != null) {
                out.close();
            }

            if (in != null) {
                in.close();
            }

            if (client != null) {
                client.close();
            }

            if (listener != null) {
                listener.close();
            }

            clientTxtArea.append("Connection closed.\n");

        } catch (IOException ioe) {

            clientTxtArea.append(
                    "IO Exception: " + ioe.getMessage() + "\n"
            );
        }
    }

    // Continuously read from the client
    public void processClient() {

        do {

            try {

                // Read message from client
                msg = (String) in.readObject();

                clientTxtArea.append(
                        "From CLIENT >> " + msg + "\n"
                );

                if (!msg.equalsIgnoreCase("Exit")) {

                    upCaseMsg = msg.toUpperCase();

                    sendData(upCaseMsg);

                    // Server can also type a message using Scanner
                    System.out.print("Enter message to send to client: ");
                    String serverMsg = keyboard.nextLine();

                    sendData(serverMsg);
                }

            } catch (IOException ioe) {

                clientTxtArea.append(
                        "IO Exception: " + ioe.getMessage() + "\n"
                );

                break;

            } catch (ClassNotFoundException cnfe) {

                clientTxtArea.append(
                        "Class not found: " + cnfe.getMessage() + "\n"
                );

                break;
            }

        } while (!msg.equalsIgnoreCase("Exit"));

        closeConnection();
    }

    public static void main(String[] args) {

        GuiServerApp server = new GuiServerApp();

        server.setSize(500, 300);
        server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        server.setVisible(true);

        try {

            server.listenForClients();
            server.processClient();

        } catch (IOException ioe) {

            System.out.println(
                    "Error: " + ioe.getMessage()
            );
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == exitBtn) {

            sendData("Exit");

            closeConnection();

            System.exit(0);
        }
    }

}
