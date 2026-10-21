package za.ac.cput.prac4;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.*;
import za.ac.cput.domain.*;
import za.ac.cput.dao.StudentDAO;

public class ServerApp extends JFrame {

    private ServerSocket listener;
    private String severMsg = "";
    private Object clientMsg = "";
    private ObjectOutputStream out;
    private ObjectInputStream in;
    ArrayList<Student> student = new ArrayList<>();

    // student object
    private StudentDAO studentDB;

    // Client connection
    private Socket client;

    public ServerApp() {
        // Create server socket
        try {
            listener = new ServerSocket(6666);
            studentDB = new StudentDAO(); // create DAO object

        } catch (IOException ioe) {
            System.out.println("IO Exception: " + ioe.getMessage());
        }

    }

    //declare a method to listen for client connections
    private void listenForClients() throws IOException {
        try {
            client = listener.accept();
            createStreams();
        } catch (IOException ioe) {
            System.out.println("IO Exception: " + ioe.getMessage());
        }
    }

    //declare a method to initiate communication streams
    private void createStreams() throws IOException {
        try {
            out = new ObjectOutputStream(client.getOutputStream());
            out.flush();
            in = new ObjectInputStream(client.getInputStream());
        } catch (IOException ioe) {
            System.out.println("IO Exception: " + ioe.getMessage());
        }
    }

    public void processClient() throws IOException {
        try {
            clientMsg = in.readObject();

            while (!clientMsg.equals("EXIT")) {

                if (clientMsg instanceof Student) {
                    Student student = (Student) clientMsg;
                    studentDB.insert(student);

                    System.out.println("Student record added.");
                } else if (clientMsg instanceof String) {
                    String request = (String) clientMsg;

                    if (request.equals("RETRIEVE")) {
                        ArrayList<Student> students = studentDB.select();
                        out.writeObject(students);
                        out.flush();
                    }
                }

                clientMsg = in.readObject();
            }

        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        } catch (EOFException e) {
            System.out.println("Client disconnected.");
        }
    }

    public static void main(String[] args) {
        // Create application
        ServerApp server = new ServerApp();

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

}// end of class
