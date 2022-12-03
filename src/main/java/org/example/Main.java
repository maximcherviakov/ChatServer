package org.example;

public class Main {
    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        Thread chatThread = new Thread(server);
        chatThread.start();
    }
}