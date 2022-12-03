package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ChatServer implements Runnable {
    private static final int PORT = 8893;

    private static final Map<Integer, Socket> clientMap = new HashMap<>();

    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server run. Waiting for clients...");
            int numberOfClient = 1;

            while (true) {
                Socket clientSocket = serverSocket.accept();

                Thread clientThread = new Thread(new ClientThread(clientSocket, this, numberOfClient));
                clientThread.setDaemon(true);
                clientThread.start();
                clientMap.put(numberOfClient, clientSocket);
                numberOfClient++;
            }
        } catch (IOException e) {
            System.err.println("Something went wrong during connecting new client");
            e.printStackTrace();
        }
    }

    public static void removeClient(int number) {
        clientMap.remove(number);
    }

    public void sendMessageForAllClients(int numberOfClient, String clientMessage) {
        for (var entry : clientMap.entrySet()) {
            if (numberOfClient != entry.getKey()) {
                System.out.println("Sending message to Client " + entry.getKey());
                try {
                    new PrintWriter(entry.getValue().getOutputStream(), true).
                            println("Client " + numberOfClient + ": " + clientMessage);
                } catch (IOException e) {
                    System.err.println("Something went wrong while sending message from client " + numberOfClient);
                    e.printStackTrace();
                }
            }
        }
    }
}
