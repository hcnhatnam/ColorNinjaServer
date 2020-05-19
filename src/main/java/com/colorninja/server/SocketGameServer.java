package com.colorninja.server;

import com.colorninja.entity.GroupScoketPlayer;
import com.colorninja.buissiness.output.BaseOutPacketInstance;
import com.colorninja.entity.SocketPlayer;
import com.colorninja.entity.Utils;
import com.colorninja.buissiness.IOSocket;
import com.colorninja.buissiness.InGame;
import com.colorninja.input.BaseInPacket;
import com.colorninja.input.KeyPlayerGroupModePacket;
import com.colorninja.input.KeyPlayerPacket;
import com.colorninja.buissiness.output.OutBoardInfoPacket;
import com.colorninja.buissiness.output.OutNewBoardPacket;
import com.colorninja.buissiness.output.OutNewBoardPacket.PREVIOUS_STATE;
import com.colorninja.buissiness.output.WaitingPlayerGroupModePacket;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
    private static final Map<String, List<SocketPlayer>> availablePlayerGroupMode = new ConcurrentHashMap<>();

    private static final Map<String, GroupScoketPlayer> groupScoketPlayers = new ConcurrentHashMap<>();
    private static final Map<String, String> keyPlayer_GroupId = new ConcurrentHashMap<>();

    private static class Handler implements Runnable {

        private Socket socket;
        SocketPlayer socketPlayer;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        enum ErrorKeyPlayer {
            EXISTED, INVALID, VALID
        }

        ErrorKeyPlayer isValidKeyPlayer(KeyPlayerPacket keyPlayerPacket) {
            String key = keyPlayerPacket.getKeyPlayer();
            if (key == null || key.isEmpty()) {
                return ErrorKeyPlayer.INVALID;
            }

            return isExistPlayer(key) ? ErrorKeyPlayer.EXISTED : ErrorKeyPlayer.VALID;
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

        public void startGame(String groupId, List<SocketPlayer> socketPlayerInGroup) {
            GroupScoketPlayer groupScoketPlayer = new GroupScoketPlayer(groupId, socketPlayerInGroup);
            groupScoketPlayers.put(groupId, groupScoketPlayer);
            Map<String, String> key_usernames = new HashMap<>();

            for (SocketPlayer player : socketPlayerInGroup) {
                key_usernames.put(player.getKey(), player.getUserName());
                keyPlayer_GroupId.put(socketPlayer.getKey(), groupId);
            }

            OutBoardInfoPacket outBoardInfoPacket = new OutBoardInfoPacket(groupId, key_usernames);
            IOSocket.broadcast(groupScoketPlayer.getSocketPlayers().values(), outBoardInfoPacket);
            Map<OutNewBoardPacket.PREVIOUS_STATE, OutNewBoardPacket> mOut = OutNewBoardPacket.getInstances(1);
            IOSocket.broadcast(groupScoketPlayer.getSocketPlayers().values(), mOut.get(PREVIOUS_STATE.WIN));

        }

        public boolean processKeyInputAndIsContinue(PrintWriter out, KeyPlayerPacket keyPlayerPacket) {
            ErrorKeyPlayer errorKeyPlayer = isValidKeyPlayer(keyPlayerPacket);

            if (null != errorKeyPlayer) {
                switch (errorKeyPlayer) {
                    case VALID:
                        return true;
                    case INVALID:
                        IOSocket.send(out, BaseOutPacketInstance.ERROR_KEY);
                        return false;
                    case EXISTED:
                        IOSocket.send(out, BaseOutPacketInstance.PLAYER_KEY_EXISTED);
                        break;
                    default:
                        break;
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
                    IOSocket.send(out, BaseOutPacketInstance.REQUIRE_KEY);
                    Optional<BaseInPacket> opKequireKey = IOSocket.reciver(in);
                    if (opKequireKey.isPresent()) {
                        BaseInPacket baseInPacket = opKequireKey.get();
                        if (baseInPacket.getEType() == BaseInPacket.EInType.GET_KEY) {
                            KeyPlayerPacket keyPlayerPacket = (KeyPlayerPacket) baseInPacket;
                            if (processKeyInputAndIsContinue(out, keyPlayerPacket)) {
                                socketPlayer = new SocketPlayer(keyPlayerPacket.getKeyPlayer(), keyPlayerPacket.getUsername(), out, in, 0);
                                if (!availablePlayer.isEmpty()) {
                                    SocketPlayer avalPlayer = availablePlayer.get(availablePlayer.size() - 1);
                                    String keyGroup = avalPlayer.getKey() + "_" + socketPlayer.getKey();
                                    startGame(keyGroup, Arrays.asList(avalPlayer, socketPlayer));
                                    availablePlayer.remove(availablePlayer.size() - 1);

                                } else {
                                    availablePlayer.add(socketPlayer);
                                    IOSocket.send(socketPlayer, BaseOutPacketInstance.WAITING_PLAYER);
                                }
                                break;
                            }
                            return;

                        } else if (baseInPacket.getEType() == BaseInPacket.EInType.GET_KEY_GROUP_MODE) {
                            KeyPlayerGroupModePacket keyPlayerPacket = (KeyPlayerGroupModePacket) baseInPacket;
                            if (processKeyInputAndIsContinue(out, keyPlayerPacket)) {
                                socketPlayer = new SocketPlayer(keyPlayerPacket.getKeyPlayer(), keyPlayerPacket.getUsername(), out, in, 0);
                                List<SocketPlayer> playerInGroup = availablePlayerGroupMode.get(keyPlayerPacket.getGroupId());
                                String groupId = keyPlayerPacket.getGroupId();
                                if (!groupId.isEmpty() && playerInGroup != null) {
                                    playerInGroup.add(socketPlayer);
                                    startGame(groupId, playerInGroup);
                                    availablePlayerGroupMode.remove(keyPlayerPacket.getGroupId());

                                } else {
                                    String idGroup = "";
                                    for (int i = 0; i < Integer.MAX_VALUE; i++) {
                                        if (groupScoketPlayers.get(i + "") != null) {
                                            idGroup = i + "";
                                        }
                                    }
                                    availablePlayerGroupMode.put(idGroup, Arrays.asList(socketPlayer));
                                    IOSocket.send(socketPlayer, new WaitingPlayerGroupModePacket(idGroup));
                                }
                                break;
                            }
                            return;
                        } else {
                            IOSocket.send(out, BaseOutPacketInstance.UNKNOW_REQUEST);
                            return;
                        }
                    } else {
                        IOSocket.send(out, BaseOutPacketInstance.ERROR_KEY);
                        return;
                    }
                }
                while (true) {
                    Optional<BaseInPacket> opBsPacket = IOSocket.reciver(socketPlayer.getIn());
                    if (opBsPacket.isPresent()) {
                        if (opBsPacket.get().getEType() == BaseInPacket.EInType.CLOSE) {
                            return;
                        } else {
                            String keyPlayer = socketPlayer.getKey();
                            InGame.INSTANCE.process(opBsPacket.get(), groupScoketPlayers.get(keyPlayer_GroupId.get(keyPlayer)), keyPlayer);
                        }
                    } else {
                        IOSocket.send(socketPlayer, BaseOutPacketInstance.UNKNOW_REQUEST_AFTER_CONNECT);
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
                    if (socketPlayer != null) {
                        String groupId = keyPlayer_GroupId.get(socketPlayer.getKey());
                        if (groupScoketPlayers.get(groupId) != null) {
                            if (groupScoketPlayers.get(groupId).getSocketPlayers().isEmpty()) {
                                groupScoketPlayers.remove(groupId);
                                keyPlayer_GroupId.remove(socketPlayer.getKey());
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
                    availablePlayer.remove(socketPlayer);
                }
                LOGGER.info("Close connect of User: " + socketPlayer.getKey());
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
