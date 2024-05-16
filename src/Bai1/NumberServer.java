package Bai1;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NumberServer {
    private static final int PORT = 12346;
    private static final int MAX_CLIENTS = 10;
    private ServerSocket serverSocket;
    private ExecutorService executorService;

    public NumberServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            executorService = Executors.newFixedThreadPool(MAX_CLIENTS);
            System.out.println("Server started...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                executorService.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                for (int i = 1; i <= 1000; i++) {
                    out.println(i);
                    Thread.sleep(1000);
                }
                clientSocket.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new NumberServer();
    }
}
