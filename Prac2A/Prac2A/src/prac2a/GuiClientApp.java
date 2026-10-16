package prac2a;

import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import javax.swing.*;

public class GuiClientApp extends JFrame {

    private Socket server;

    private JLabel lblEnterText = new JLabel("Enter text here");
    private JTextField txtEnterText = new JTextField(15);
    private JTextArea clientTxtArea = new JTextArea(5, 40);

    private JPanel pnlWrapper = new JPanel();
    private JPanel pnlTop = new JPanel();

    private JPanel pnlCenter = new JPanel();

    public GuiClientApp() {
        super("Client App");

        pnlTop.add(new JScrollPane(lblEnterText));
        pnlTop.add(txtEnterText);
        pnlCenter.add(clientTxtArea);

        add(pnlTop, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);

    }

    public static void main(String[] args) {

        GuiClientApp client = new GuiClientApp();

        client.setSize(500, 300);
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.setVisible(true);

    }

}// end of class
