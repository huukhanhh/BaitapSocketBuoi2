    package Bai2;
    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.*;
    import java.io.*;
    import java.net.*;

    public class ChatClient {
        private static final String SERVER_ADDRESS = "localhost";
        private static final int SERVER_PORT = 12346;
        private String username;
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        private JFrame frame;
        private JTextArea messageArea;
        private JTextField messageField;
        private JButton sendButton;

        public ChatClient() {
            getUsername();
            initializeGUI();
            connectToServer();
            startReceivingMessages();
        }

        private void getUsername() {
            username = JOptionPane.showInputDialog(frame, "Enter your username:", "Username", JOptionPane.PLAIN_MESSAGE);
            if (username == null || username.isEmpty()) {
                System.exit(0);
            }
        }

        private void initializeGUI() {
            frame = new JFrame("Chat Client - " + username);
            frame.setSize(500, 500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            messageArea = new JTextArea();
            messageArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(messageArea);

            JPanel panel = new JPanel(new BorderLayout());
            messageField = new JTextField();
            sendButton = new JButton("Send");

            sendButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendMessage(); // Gửi tin nhắn khi ấn nút Send
                }
            });

            messageField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sendMessage(); // Gửi tin nhắn khi nhấn Enter trên TextField
                }
            });

            panel.add(messageField, BorderLayout.CENTER);
            panel.add(sendButton, BorderLayout.EAST);

            frame.add(scrollPane, BorderLayout.CENTER);
            frame.add(panel, BorderLayout.SOUTH);
            frame.setVisible(true);
        }

        private void connectToServer() {
            try {
                socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Gửi username đến server
                out.println(username);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void startReceivingMessages() {
            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        final String finalMessage = message; // Biến "effectively final"
                        SwingUtilities.invokeLater(() -> {
                            messageArea.append(finalMessage + "\n"); // Cập nhật giao diện ở trong invokeLater()
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        private void sendMessage() {
            String message = messageField.getText().trim();
            if (!message.isEmpty()) {
                String fullMessage = username + ": " + message;
                out.println(fullMessage); // Gửi tin nhắn kèm theo username
                messageArea.append(fullMessage + "\n"); // Hiển thị tin nhắn trên giao diện client
                messageField.setText(""); // Xóa nội dung tin nhắn sau khi gửi
            }
        }



        public static void main(String[] args) {
            SwingUtilities.invokeLater(ChatClient::new);
        }
    }
