package com.colorninja.server;

import com.colorninja.Entity.GroupScoketPlayer;
import com.colorninja.Entity.OutPacket;
import com.colorninja.Entity.ResultObject;
import com.colorninja.Entity.SocketPlayer;
import com.colorninja.Entity.TypeInput;
import com.colorninja.Entity.TypeReturn;
import com.colorninja.Entity.Utils;
import com.colorninja.buissiness.IOSocket;
import com.colorninja.buissiness.InGame;
import com.colorninja.objectingame.BaseInPacket;
import com.colorninja.objectingame.KeyPlayerPacket;
import com.colorninja.objectingame.OutNewBoardPacket;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;

public class SocketGameServer {

    private static final Logger LOGGER = Logger.getLogger(SocketGameServer.class);

    private static final List<SocketPlayer> availablePlayer = Collections.synchronizedList(new ArrayList<>());
    private static final Map<String, GroupScoketPlayer> groupScoketPlayers = new ConcurrentHashMap<>();
    private static final Map<String, String> keyPlayer_GroupId = new ConcurrentHashMap<>();

    private static class Handler implements Runnable {

        private String keyPlayer = "";
        private Socket socket;
//        private Scanner in;
//        private PrintWriter out;
        SocketPlayer socketPlayer;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void log(Object content) {
            LOGGER.info(String.format("%s_%s_", keyPlayer, Utils.gson.toJson(content)));
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

        @Override
        public void run() {
            LOGGER.info("=====Start-Connect=====");
            Scanner in = null;
            PrintWriter out = null;
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new Scanner(socket.getInputStream());

                while (true) {
                    IOSocket.send(out, OutPacket.REQUIRE_KEY);
//                    Thread.sleep(500);
                    Optional<BaseInPacket> opKequireKey = IOSocket.reciver(in);
                    if (opKequireKey.isPresent()) {
                        BaseInPacket baseInPacket = opKequireKey.get();
                        LOGGER.info("======" + baseInPacket.toString());
                        if (baseInPacket.getEType() == BaseInPacket.EInType.GET_KEY) {
                            KeyPlayerPacket keyPlayerPacket = (KeyPlayerPacket) baseInPacket;
                            String key = keyPlayerPacket.getKeyPlayer();
                            if (key == null || key.isEmpty()) {
                                IOSocket.send(out, OutPacket.ERROR_KEY);
                                return;
                            }
                            keyPlayer = key;
                            if (!isExistPlayer(keyPlayer)) {
                                socketPlayer = new SocketPlayer(keyPlayer, keyPlayerPacket.getUsername(), out, in, 0);
                                if (!availablePlayer.isEmpty()) {
                                    SocketPlayer avalPlayer = availablePlayer.get(availablePlayer.size() - 1);
                                    availablePlayer.remove(availablePlayer.size() - 1);
                                    GroupScoketPlayer groupScoketPlayer = new GroupScoketPlayer(avalPlayer, socketPlayer);
                                    keyPlayer_GroupId.put(avalPlayer.getKey(), groupScoketPlayer.getIdGroup());
                                    keyPlayer_GroupId.put(socketPlayer.getKey(), groupScoketPlayer.getIdGroup());
                                    groupScoketPlayers.put(groupScoketPlayer.getIdGroup(), groupScoketPlayer);
                                    List<String> userNames = Arrays.asList(socketPlayer.getUserName(), avalPlayer.getUserName());
                                    IOSocket.broadcast(groupScoketPlayer.getSocketPlayers().values(), new OutNewBoardPacket(1, userNames));

                                } else {
                                    availablePlayer.add(socketPlayer);
                                    IOSocket.send(socketPlayer, OutPacket.WAITING_PLAYER);
                                }
                                break;
                            } else {
                                IOSocket.send(out, OutPacket.PLAYER_KEY_EXISTED);
                                return;
                            }
                        } else {
                            IOSocket.send(out, OutPacket.UNKNOW_REQUEST);
                            return;
                        }
                    } else {
                        IOSocket.send(out, OutPacket.ERROR_KEY);
                        return;
                    }
                }
                while (true) {
                    Optional<BaseInPacket> opBsPacket = IOSocket.reciver(socketPlayer.getIn());
                    if (opBsPacket.isPresent()) {
                        if (opBsPacket.get().getEType() == BaseInPacket.EInType.CLOSE) {
                            return;
                        } else {
                            InGame.INSTANCE.process(opBsPacket.get(), groupScoketPlayers.get(keyPlayer_GroupId.get(keyPlayer)), keyPlayer);
                        }
                    } else {
                        IOSocket.send(socketPlayer, OutPacket.UNKNOW_REQUEST_AFTER_CONNECT);
                        return;
                    }
                }
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }

                    if (keyPlayer != null && !keyPlayer.isEmpty()) {
                        String groupId = keyPlayer_GroupId.get(keyPlayer);
                        if (groupScoketPlayers.get(groupId) != null) {
                            if (groupScoketPlayers.get(groupId).getSocketPlayers().isEmpty()) {
                                groupScoketPlayers.remove(groupId);
                                keyPlayer_GroupId.remove(keyPlayer);
                            }
                        }

                    }
                    if (!socket.isClosed()) {
                        socket.close();

                    }
                } catch (Exception ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
                LOGGER.info("availablePlayer" + availablePlayer.toString());

                if (availablePlayer.contains(socketPlayer)) {
                    LOGGER.info("availablePlayer3" + availablePlayer.toString());

                    availablePlayer.remove(socketPlayer);
                }
                LOGGER.info("availablePlayer2" + availablePlayer.toString());

                LOGGER.info("Close connect of User: " + keyPlayer);
            }

        }
    }

    public static void main(String[] args) throws IOException {
        LOGGER.info("The chat server is running...8080");
        ExecutorService pool = Executors.newFixedThreadPool(20);
        try (ServerSocket listener = new ServerSocket(8080)) {
            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        }
    }

}
