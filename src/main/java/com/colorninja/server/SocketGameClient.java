/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.server;

import com.colorninja.Entity.ResultObject;
import com.colorninja.Entity.Utils;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author namhcn
 */
public class SocketGameClient {

    String serverAddress;
    Scanner in;
    PrintWriter out;
    String name;

    public SocketGameClient(String serverAddress, String name) {
        this.serverAddress = serverAddress;
        this.name = name;
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
        System.err.println(name + "_" + getDate() + "clientSend" + jsonObject.toString());
    }

    public void run() throws IOException, InterruptedException {
        try {
            Socket socket = new Socket(serverAddress, 8080);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                Map<String, Object> resultObject = Utils.gson.fromJson(line, Map.class);
                System.err.println("resultObject" + resultObject);
                if ((double) resultObject.get("type") == 2) {
                    JsonObject jo = new JsonObject();
                    jo.addProperty("type", 2);
                    jo.addProperty("keyPlayer", name);
                    jo.addProperty("username", name + "username");

                    out.println(jo.toString());
//                    if (name.contains("Nam")) {
//                        out.close();
//
//                    }
//                    send(out, "get_key", jo);
                } else if ((double) resultObject.get("type") == 5) {
                    System.err.println(name + "_" + getDate() + resultObject);
//                    Thread.sleep(1000 + Utils._randomColor.nextInt(4000));
                    JsonObject jo = new JsonObject();
                    jo.addProperty("type", 0);
                    Map m = (Map) resultObject.get("boardGame");
                    jo.addProperty("round", (Double) m.get("round"));
                    out.println(jo.toString());

                } else if ((double) resultObject.get("type") == 4) {
//                    System.err.println("Close Socket");
//                    JsonObject jo = new JsonObject();
//                    jo.addProperty("type", 1);
//                    out.println(jo.toString());
                } else {
                    System.err.println(name + "_" + getDate() + resultObject);
                }

            }
        } finally {
        }
    }

    public static String getDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    public static void main(String[] args) throws Exception {
        SocketGameClient client = new SocketGameClient("119.82.135.105", "Nam" + Utils._randomColor.nextInt(4000));
        Thread thread = new Thread() {
            public void run() {
                try {
                    client.run();
                } catch (Exception ex) {
                }
            }
        };
        thread.start();
        Thread.sleep(1000);
        SocketGameClient client2 = new SocketGameClient("119.82.135.105", "Quy" + Utils._randomColor.nextInt(4000));
        Thread thread2 = new Thread() {
            public void run() {
                try {
                    client2.run();
                } catch (Exception ex) {
                }
            }
        };
        thread2.start();
    }
}
