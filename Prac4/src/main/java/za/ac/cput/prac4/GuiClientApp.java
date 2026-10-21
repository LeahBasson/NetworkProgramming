package za.ac.cput.prac4;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.*;
import za.ac.cput.domain.*;

public class GuiClientApp extends JFrame implements ActionListener {

    private Socket server;
    private String response = "";
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private JLabel lblName = new JLabel("Name:");
    private JTextField txtName = new JTextField(18);
    private JLabel lblID = new JLabel("ID:");
    private JTextField txtID = new JTextField(18);
    private JLabel lblScore = new JLabel("Score:");
    private JTextField txtScore = new JTextField(18);
    private JTextArea txtRecords = new JTextArea(5, 40);

    private JButton btnAdd = new JButton("Add record");
    private JButton btnRetrieve = new JButton("Retrieve Records");
    private JButton btnExit = new JButton("Exit");
    private JPanel pnlFields = new JPanel();
    private JPanel pnlButtons = new JPanel();
    private JPanel pnlRecords = new JPanel();

    public GuiClientApp() {
        super("Student Record Management");

        setLayout(new GridLayout(3, 1));
        pnlFields.setLayout(new GridLayout(3, 2));

        pnlFields.add(lblName);
        pnlFields.add(txtName);
        pnlFields.add(lblID);
        pnlFields.add(txtID);
        pnlFields.add(lblScore);
        pnlFields.add(txtScore);

        add(pnlFields);

        pnlButtons.add(btnAdd);
        pnlButtons.add(btnRetrieve);
        pnlButtons.add(btnExit);
        add(pnlButtons);

        btnAdd.addActionListener(this);
        btnRetrieve.addActionListener(this);
        btnExit.addActionListener(this);
        
        pnlRecords.add(txtRecords);
        add(pnlRecords);

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
            System.out.println("IOException: " + ioe.getMessage());
        }
    }

    //declare a method in which you write data to the server    
    private void sendData(Object student) {
        try {
            out.writeObject(student);
        } catch (IOException ioe) {
            System.out.println("IOException: " + ioe.getMessage());
        }
    }

    //declare a method in which you continuously read from the server.
    public void communicate() throws ClassNotFoundException {
        try {
            ArrayList<Student> records = (ArrayList<Student>) in.readObject();

            txtRecords.setText("");
            for (Student s : records) {
                txtRecords.append("Name: " + s.getStudName()
                        + ", ID: " + s.getStudId()
                        + ", Score: " + s.getScore() + "\n");
            }
        } catch (IOException ioe) {
            System.out.println("IOException: " + ioe.getMessage());
        }
    }

    //declare the actionPerformed method in which you send the required data to the server 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAdd) {
            String name = txtName.getText();
            String id = txtID.getText();
            String score = txtScore.getText();

            Student student = new Student(name, id, score);
            sendData(student);

            txtName.setText("");
            txtID.setText("");
            txtScore.setText("");
        }

        if (e.getSource() == btnRetrieve) {
            sendData("RETRIEVE");
            try {
                communicate();
            } catch (ClassNotFoundException ex) {
                System.out.println("Class not found: " + ex.getMessage());
            }
        }

        if (e.getSource() == btnExit) {
            sendData("EXIT");

            try {
                in.close();
                out.close();
                server.close();
            } catch (IOException ioe) {
                System.out.println("IOException: " + ioe.getMessage());
            }

            dispose();
        }
    }

    //declare a method in which you close the streams and the socket connection
    public static void main(String[] args) {

        GuiClientApp client = new GuiClientApp();

        client.setSize(500, 300);
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.setVisible(true);

    }

}// end of class
