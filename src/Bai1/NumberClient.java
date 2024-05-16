package Bai1;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class NumberClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12346;
    private Socket socket;
    private BufferedReader in;

    private JFrame frame;
    private JTextArea textArea;

    public NumberClient() {
        initializeGUI();
        connectToServer();
        startReceivingNumbers();
    }

    private void initializeGUI() {
        frame = new JFrame("Number Client");
        frame.setSize(300, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void connectToServer() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReceivingNumbers() {
        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    textArea.append(line + "\n");
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NumberClient::new);
    }
}
