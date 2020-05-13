/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninjaserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author namhcn
 */
public class ChatClient {

    String serverAddress;
    Scanner in;
    PrintWriter out;

    /**
     * Constructs the client by laying out the GUI and registering a listener
     * with the textfield so that pressing Return in the listener sends the
     * textfield contents to the server. Note however that the textfield is
     * initially NOT editable, and only becomes editable AFTER the client
     * receives the NAMEACCEPTED message from the server.
     */
    public ChatClient(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    private String getName() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Printing the file passed in:");
        String str = sc.nextLine();
        return str;
    }

    private void run() throws IOException {
        try {
            Socket socket = new Socket(serverAddress, 6565);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            String name = "";
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.startsWith("SUBMITNAME")) {
                    name = getName();
                    out.println(name);
                } else if (line.startsWith("NAMEACCEPTED")) {
                    System.err.println("Chatter - " + name);
//                    textField.setEditable(true);
                } else if (line.startsWith("MESSAGE")) {
                    System.err.println("Server send: - " + line);
//                    messageArea.append(line.substring(8) + "\n");
                }
            }
        } finally {
        }
    }

    public static void main(String[] args) throws Exception {
        ChatClient client = new ChatClient("127.0.0.1");
        client.run();
    }
}
