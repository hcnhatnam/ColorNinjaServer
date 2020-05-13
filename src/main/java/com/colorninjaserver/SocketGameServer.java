package com.colorninjaserver;

import com.Entity.BoardGame;
import com.Entity.GroupScoketPlayer;
import com.Entity.ResultObject;
import com.Entity.SocketPlayer;
import com.Entity.TypeInput;
import com.Entity.TypeReturn;
import com.Entity.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.JsonAdapter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketGameServer {

//    private static Set<String> names = new HashSet<>();
//
//    private static Set<PrintWriter> writers = new HashSet<>();
    private static List<SocketPlayer> availablePlayer = Collections.synchronizedList(new ArrayList<>());
    private static Map<String, GroupScoketPlayer> groupScoketPlayers = new ConcurrentHashMap<>();

    private static class Handler implements Runnable {

        private String keyPlayer;
        private Socket socket;
        private Scanner in;
        private PrintWriter out;
        private String groupId;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public boolean isExistPlayer(String key) {
            for (Map.Entry<String, GroupScoketPlayer> entry : groupScoketPlayers.entrySet()) {
                GroupScoketPlayer groupScoketPlayer = entry.getValue();
                for (Map.Entry<String, SocketPlayer> entry1 : groupScoketPlayer.getSocketPlayers().entrySet()) {
                    SocketPlayer socketPlayer = entry1.getValue();
                    if (socketPlayer.getKey().equals(key)) {
                        return true;
                    }
                }
            }
            for (SocketPlayer socketPlayer : availablePlayer) {
                if (socketPlayer.getKey().equals(key)) {
                    return true;
                }
            }
            return false;
        }

        public void broadcast(Map<String, SocketPlayer> socketPlayers, ResultObject resultObject) {
            for (Map.Entry<String, SocketPlayer> entry : socketPlayers.entrySet()) {
                SocketPlayer socketPlayer = entry.getValue();
                send(socketPlayer.getPrintWriter(), resultObject);
            }
        }

        public void send(PrintWriter printWriter, ResultObject resultObject) {
            printWriter.println(resultObject.toString());
        }

        @Override
        public void run() {
            try {
                in = new Scanner(socket.getInputStream());
                out = new PrintWriter(socket.getOutputStream(), true);

                while (true) {
                    send(out, TypeReturn.REQUIRE_KEY);
                    String input = in.nextLine();
                    TypeInput.ETypeInput eTypeInput = TypeInput.get(input);
                    if (eTypeInput == TypeInput.ETypeInput.GET_KEY) {
                        JsonObject jsonObject = Utils.gson.fromJson(input, JsonObject.class);
                        String key = jsonObject.get("key").getAsString();
                        if (key == null || key.isEmpty()) {
                            send(out, TypeReturn.ERROR_KEY);
                            return;
                        }
                        keyPlayer = key;
                        if (!isExistPlayer(keyPlayer)) {
                            SocketPlayer socketPlayer = new SocketPlayer(keyPlayer, out, in, 0);
                            if (!availablePlayer.isEmpty()) {
                                SocketPlayer avalPlayer = availablePlayer.get(availablePlayer.size() - 1);
                                availablePlayer.remove(availablePlayer.size() - 1);
                                GroupScoketPlayer groupScoketPlayer = new GroupScoketPlayer(avalPlayer, socketPlayer);
                                groupScoketPlayers.put(groupScoketPlayer.getIdGroup(), groupScoketPlayer);
                                groupId = groupScoketPlayer.getIdGroup();
                                broadcast(groupScoketPlayer.getSocketPlayers(), TypeReturn.getResultObjectBoardGame(groupScoketPlayer.getRound()));
                            } else {
                                availablePlayer.add(socketPlayer);
                                send(out, TypeReturn.WAITING_PLAYER);

                            }
                            break;
                        } else {
                            send(out, TypeReturn.PLAYER_KEY_EXISTED);
                            return;
                        }
                    } else {
                        send(out, TypeReturn.ERROR_TYPE);
                        return;
                    }
                }

                while (true) {
                    String input = in.nextLine();
                    TypeInput.ETypeInput eTypeInput = TypeInput.get(input);
                    if (eTypeInput == TypeInput.ETypeInput.WIN) {
                        GroupScoketPlayer groupScoketPlayer = groupScoketPlayers.get(groupId);
                        Map<String, SocketPlayer> mSo = groupScoketPlayer.getSocketPlayers();
                        SocketPlayer socketPlayer = mSo.get(keyPlayer);
                        socketPlayer.setScore(socketPlayer.getScore() + 1);
                        int currentRount = groupScoketPlayer.getRound();
                        if (currentRount == 3) {
                            String winnerKey = "";
                            int maxScore = 0;
                            JsonArray jsonArray = new JsonArray();
                            for (Map.Entry<String, SocketPlayer> entry : mSo.entrySet()) {
                                String key = entry.getKey();
                                SocketPlayer sk = entry.getValue();
                                if (sk.getScore() > maxScore) {
                                    winnerKey = key;
                                    maxScore = sk.getScore();
                                }
                                JsonObject jsonElement = new JsonObject();
                                jsonElement.addProperty(key, sk.getScore());
                                jsonArray.add(jsonElement);
                            }
                            System.err.println(mSo);
                            broadcast(mSo, TypeReturn.getResultObjectWinGame(winnerKey, maxScore, jsonArray));
                            return;
                        } else {
                            System.err.println(mSo);
                            groupScoketPlayer.setRound(currentRount + 1);
                            broadcast(mSo, TypeReturn.getResultObjectBoardGame(currentRount + 1));
                        }

                    } else if (eTypeInput == TypeInput.ETypeInput.CLOSE) {
                        return;
                    }
                }

            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (out != null) {
                    if (groupScoketPlayers.get(groupId) != null) {
                        if (groupScoketPlayers.get(groupId).getSocketPlayers().isEmpty()) {
                            groupScoketPlayers.remove(groupId);
                        }
                    }
                }
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("The chat server is running...");
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try (ServerSocket listener = new ServerSocket(6565)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        }
    }

}
