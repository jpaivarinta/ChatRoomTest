package com.company;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String host = "localhost";
    private static final int portNumber = 4444;
    private String userName;
    private String serverHost;
    private int serverPort;
    private Scanner userInputScanner;


    public static void main(String[] args) {

        String readName = null;
        Scanner sc = new Scanner(System.in);
        System.out.println("Username: ");
        readName = sc.nextLine();
        Client client = new Client(readName, host, portNumber);
        client.startClient(sc);

    }

    public Client(String userName, String host, int portNumber) {
        this.userName = userName;
        this.serverHost = host;
        this.serverPort = portNumber;
    }

    private void startClient(Scanner sc) {
        try {
            Socket socket = new Socket(serverHost, serverPort);
            Thread.sleep(1000);

            ServerThread serverThread = new ServerThread(socket, userName);
            Thread serverAccessThread = new Thread(serverThread);
            serverAccessThread.start();
            while (serverAccessThread.isAlive()) {
                if (sc.hasNextLine()){
                    serverThread.addNextMessage(sc.nextLine());
                }
            }
        }
        catch(IOException e) {
            System.err.println("Fatal connection error!");
            e.printStackTrace();
        }
        catch(InterruptedException e) {
            System.err.println("Fatal connection error!");
            e.printStackTrace();
        }
    }

}
