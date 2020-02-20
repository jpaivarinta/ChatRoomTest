package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ChatServer {

    private static final int portNumber = 4444;
    private int serverPort;
    private List<ClientThread> clients;

    public static void main(String[] args) {
        ChatServer server = new ChatServer(portNumber);
        server.startServer();
    }

    public ChatServer(int portNumber) {
        this.serverPort = portNumber;
    }

    public List<ClientThread> getClients() {
        return clients;
    }

    private void startServer() {
        clients = new ArrayList<ClientThread>();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);
            acceptClients(serverSocket);
        }
        catch (IOException e) {
            System.err.println("Error on listening port: "+portNumber);
            System.exit(1);
        }
    }


    private void acceptClients(ServerSocket serverSocket) {
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                ClientThread client = new ClientThread(this, socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
            }
            catch (IOException e) {
                System.out.println("Accept failed on :" + portNumber);
            }
        }
    }
}
