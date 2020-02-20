package com.company;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

public class ServerThread implements Runnable{


    private Socket socket;
    private String name;
    private boolean isAlive;
    private boolean hasMessages = false;
    private final LinkedList<String> messagesToSend;

    public ServerThread(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
        messagesToSend = new LinkedList<String>();
    }

    public void addNextMessage(String msg) {
        synchronized (messagesToSend) {
            hasMessages = true;
            messagesToSend.push(msg);
        }
    }


    @Override
    public void run() {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), false);
            InputStream serverInStream = socket.getInputStream();
            Scanner serverIn = new Scanner(serverInStream);

            while (!socket.isClosed()) {
                if(serverInStream.available() > 0) {
                    if (serverIn.hasNextLine()) {
                        System.out.println(serverIn.nextLine());
                    }
                }
                if(hasMessages){
                    String next = "";
                    synchronized(messagesToSend){
                        next = messagesToSend.pop();
                        hasMessages = !messagesToSend.isEmpty();
                    }
                    out.println(name + " > " + next);
                    out.flush();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
