/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninjaserver;

import com.Entity.ResultObject;
import com.Entity.TypeReturn;
import com.Entity.Utils;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author namhcn
 */
public class SocketGameClient2 {

    String serverAddress;
    Scanner in;
    PrintWriter out;

    public SocketGameClient2(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    private String getName() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Printing the file passed in:");
        String str = sc.nextLine();
        return str;
    }

    public void send(PrintWriter printWriter, String type, JsonObject jsonObject) {
        jsonObject.addProperty("type", type);
        printWriter.println(jsonObject.toString());
    }

    private void run() throws IOException, InterruptedException {
        try {
            Socket socket = new Socket(serverAddress, 6565);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            String name = "";
            while (in.hasNextLine()) {
                String line = in.nextLine();
                ResultObject resultObject = Utils.gson.fromJson(line, ResultObject.class);
                if (resultObject.getType().equals("require_key")) {
                    JsonObject jo = new JsonObject();
                    jo.addProperty("key", "Quy");
                    send(out, "get_key", jo);
                } else if (resultObject.getType().equals("board_game")) {
                    System.err.println(resultObject);
                    Thread.sleep(1000 + Utils._randomColor.nextInt(4000));
                    JsonObject jo = new JsonObject();
                    send(out, "win", jo);
                } else {
                    System.err.println(resultObject);
                }

            }
        } finally {
        }
    }

    public static void main(String[] args) throws Exception {
        SocketGameClient2 client = new SocketGameClient2("127.0.0.1");
        client.run();
    }
}
