/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.model;

import com.colorninja.entity.Utils;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

/**
 *
 * @author namhcn
 */
public class BotGame {

    private static final Logger LOGGER = Logger.getLogger(BotGame.class);

    String serverAddress;
    Scanner in;
    PrintWriter out;
    String name;

    public BotGame(String serverAddress, String name) {
        this.serverAddress = serverAddress;
        this.name = name;
    }

    public void send(PrintWriter printWriter, String type, JsonObject jsonObject) {
        jsonObject.addProperty("type", type);
        printWriter.println(jsonObject.toString());
    }
    Double currentRound = 0d;
    public static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    public static List<String> names = Collections.synchronizedList(Arrays.asList("MongHoang", "LapHoang", "NamHoang", "QuyNguyen", "TranQuy", "TaiNgo", "HuuTai", "JohnWick", "Samuen", "Linh Do", "LinhDuc", "DucLinh"));

    public void run() throws IOException, InterruptedException {
        try {
            Socket socket = new Socket(serverAddress, 8080);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                LOGGER.error("xxxrecive:" + line);

                Map<String, Object> resultObject = Utils.gson.fromJson(line, Map.class);
                if ((double) resultObject.get("type") == 2) {
                    JsonObject jo = new JsonObject();
                    jo.addProperty("type", 2);
                    jo.addProperty("keyPlayer", name);
                    jo.addProperty("username", name);
                    out.println(jo.toString());

                } else if ((double) resultObject.get("type") == 5) {
                    JsonObject jo = new JsonObject();
                    jo.addProperty("type", 0);
                    Map m = (Map) resultObject.get("boardGame");
                    jo.addProperty("round", (Double) m.get("round"));

                    currentRound = (Double) m.get("round");
                    int delay = (1120 + (int) ((currentRound + 5) / 5) * (currentRound > 10 ? 150 : 200) + Utils._randomColor.nextInt(200));
                    LOGGER.error("deplay:" + delay);

                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                        @Override
                        public void run() {
                            LOGGER.error("xxxSendWin:" + jo.toString());
                            out.println(jo.toString());
                        }
                    },
                            currentRound == 1 ? 4000 : delay
                    );
                } else if ((double) resultObject.get("type") == 4) {
                    JsonObject jo = new JsonObject();
                    jo.addProperty("type", 0);
                    Map m = (Map) resultObject.get("boardGame");
                } else if ((double) resultObject.get("type") == 6) {
                    //infogroup
                }

//                else if ((double) resultObject.get("type") == -3) {
//                    Thread.sleep(300 + Utils._randomColor.nextInt(500));
//
//                    JsonObject jo = new JsonObject();
//                    jo.addProperty("type", 0);
//                    jo.addProperty("round", ++currentRound);
//                    out.println(jo.toString());
//                } else if ((double) resultObject.get("type") == -4) {
//                    Thread.sleep(300 + Utils._randomColor.nextInt(500));
//
//                    JsonObject jo = new JsonObject();
//                    jo.addProperty("type", 0);
//                    jo.addProperty("round", --currentRound);
//                    out.println(jo.toString());
//                }
                LOGGER.error("xxxwating:");
            }
        } finally {
        }
    }

    public static void startBoot() {
        try {
            LOGGER.error("bottttttttttttttttttttttttttttt");

            BotGame client = new BotGame("127.0.0.1", names.get(Utils._randomColor.nextInt(names.size() - 1)));
            Thread thread = new Thread() {
                public void run() {
                    try {
                        client.run();
                    } catch (Exception ex) {
                    }
                }
            };
            thread.start();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    public static void main(String[] args) {

        System.err.println(11 / 10);
    }
//    public static void main(String[] args) throws Exception {
//        SocketGameClient client = new SocketGameClient("127.0.0.1", "BotNam" + Utils._randomColor.nextInt(4000));
//        Thread thread = new Thread() {
//            public void run() {
//                try {
//                    client.run();
//                } catch (Exception ex) {
//                }
//            }
//        };
//        thread.start();
////        Thread.sleep(1000);
////        SocketGameClient client2 = new SocketGameClient("127.0.0.1", "Quy" + Utils._randomColor.nextInt(4000));
////        Thread thread2 = new Thread() {
////            public void run() {
////                try {
////                    client2.run();
////                } catch (Exception ex) {
////                }
////            }
////        };
////        thread2.start();
//    }
}
