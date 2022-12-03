 package org.example;

 import java.io.BufferedReader;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.io.PrintWriter;
 import java.net.Socket;

 public class ClientThread implements Runnable {
     private final Socket clientSocket;
     private final ChatServer chatServer;
     private final int numberOfClient;

     public ClientThread(Socket clientSocket, ChatServer chatServer, int numberOfClient) {
         this.clientSocket = clientSocket;
         this.chatServer = chatServer;
         this.numberOfClient = numberOfClient;
     }

     @Override
     public void run() {
         try (BufferedReader inReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
             System.out.println("Client " + numberOfClient + " was connected");
             new PrintWriter(clientSocket.getOutputStream(), true).println("Client " + numberOfClient);
             while (true) {
                 String clientMessage = inReader.readLine();

                 if (!"exit".equals(clientMessage)) {
                     System.out.println("Client " + numberOfClient + ": " + clientMessage);
                     chatServer.sendMessageForAllClients(numberOfClient, clientMessage);
                 } else {
                     System.out.println("Client " + numberOfClient + " was disconnected");
                     ChatServer.removeClient(numberOfClient);
                     break;
                 }
             }
         } catch (IOException e) {
             System.err.println("Something went wrong during getting input stream from client");
             e.printStackTrace();
         }
     }
 }
