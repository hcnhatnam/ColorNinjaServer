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
import com.colorninja.entity.SlotLock;
import com.server.model.BotGame;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;

public class SocketGameServer {

    private static final Logger LOGGER = Logger.getLogger(SocketGameServer.class);

    private static final List<SocketPlayer> availablePlayer = Collections.synchronizedList(new ArrayList<>());
    private static final Map<String, List<SocketPlayer>> availablePlayerGroupMode = new ConcurrentHashMap<>();

    public static final Map<String, GroupScoketPlayer> groupScoketPlayers = new ConcurrentHashMap<>();
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
            try {
                LOGGER.info("isValidKeyPlayer:" + keyPlayerPacket);
                String key = keyPlayerPacket.getKeyPlayer();
                if (key == null || key.isEmpty()) {
                    return ErrorKeyPlayer.INVALID;
                }

                return isExistPlayer(key) ? ErrorKeyPlayer.EXISTED : ErrorKeyPlayer.VALID;
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            return ErrorKeyPlayer.INVALID;
        }

        public boolean isExistPlayer(String key) {
            try {
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
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
            return false;
        }

        public void startGame(String groupId, List<SocketPlayer> socketPlayerInGroup) {
            try {

                GroupScoketPlayer groupScoketPlayer = new GroupScoketPlayer(groupId, socketPlayerInGroup);
                groupScoketPlayers.put(groupId, groupScoketPlayer);
                Map<String, String> key_usernames = new HashMap<>();

                for (SocketPlayer player : socketPlayerInGroup) {
                    LOGGER.error("--------------" + player.getUserName());
                    key_usernames.put(player.getKey(), player.getUserName());
                    keyPlayer_GroupId.put(player.getKey(), groupId);
                }

                OutBoardInfoPacket outBoardInfoPacket = new OutBoardInfoPacket(groupId, key_usernames);
                IOSocket.broadcast(groupScoketPlayer.getSocketPlayers().values(), outBoardInfoPacket);
                Map<OutNewBoardPacket.PREVIOUS_STATE, OutNewBoardPacket> mOut = OutNewBoardPacket.getInstances(1);
                IOSocket.broadcast(groupScoketPlayer.getSocketPlayers().values(), mOut.get(PREVIOUS_STATE.WIN));
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }

        public boolean processKeyInputAndIsContinue(PrintWriter out, KeyPlayerPacket keyPlayerPacket) {
            try {

                ErrorKeyPlayer errorKeyPlayer = isValidKeyPlayer(keyPlayerPacket);
                LOGGER.info("processKeyInputAndIsContinue:" + errorKeyPlayer);
                if (null != errorKeyPlayer) {
                    switch (errorKeyPlayer) {
                        case VALID:
                            return true;
                        case INVALID:
                            IOSocket.send(out, BaseOutPacketInstance.ERROR_KEY);
                            return false;
                        case EXISTED:
                            IOSocket.send(out, BaseOutPacketInstance.PLAYER_KEY_EXISTED);
                            return false;
                        default:
                            break;
                    }
                }
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
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
                                    SocketPlayer avalPlayer = availablePlayer.get(0);
                                    String keyGroup = System.currentTimeMillis() + "";
                                    List<SocketPlayer> socketPlayers = new ArrayList<>();
                                    socketPlayers.add(avalPlayer);
                                    socketPlayers.add(socketPlayer);
                                    startGame(keyGroup, socketPlayers);
                                    availablePlayer.remove(0);

                                } else {
                                    availablePlayer.add(socketPlayer);
                                    IOSocket.send(socketPlayer, BaseOutPacketInstance.WAITING_PLAYER);
                                }
                                break;
                            }
                            return;

                        } else if (baseInPacket.getEType() == BaseInPacket.EInType.GET_KEY_GROUP_MODE) {
                            try {

                                KeyPlayerGroupModePacket keyPlayerPacket = (KeyPlayerGroupModePacket) baseInPacket;
                                LOGGER.error("keyPlayerPacket" + keyPlayerPacket);
                                if (processKeyInputAndIsContinue(out, keyPlayerPacket)) {

                                    socketPlayer = new SocketPlayer(keyPlayerPacket.getKeyPlayer(), keyPlayerPacket.getUsername(), out, in, 0);
                                    String groupId = keyPlayerPacket.getGroupId();
                                    LOGGER.info("groupId" + groupId);

                                    if (groupId != null && !groupId.isEmpty()) {
                                        List<SocketPlayer> playerInGroup = availablePlayerGroupMode.get(keyPlayerPacket.getGroupId());
                                        if (playerInGroup == null) {
                                            IOSocket.send(out, BaseOutPacketInstance.GROUP_NOT_EXIST);
                                            return;
                                        }
                                        playerInGroup.add(socketPlayer);
                                        startGame(groupId, playerInGroup);
                                        availablePlayerGroupMode.remove(keyPlayerPacket.getGroupId());

                                    } else {
                                        String idGroup = "0";
                                        do {
                                            idGroup = Utils.getRandom(100) + "";
                                        } while (groupScoketPlayers.get(idGroup) != null);
                                        groupScoketPlayers.put(idGroup, new GroupScoketPlayer(idGroup, socketPlayer));

                                        List<SocketPlayer> socketPlayers = new ArrayList<>();
                                        socketPlayers.add(socketPlayer);
                                        availablePlayerGroupMode.put(idGroup, socketPlayers);
                                        IOSocket.send(socketPlayer, new WaitingPlayerGroupModePacket(idGroup));
                                    }

                                    break;
                                }
                            } catch (Exception ex) {
                                LOGGER.error(ex.getMessage(), ex);
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
                            String groupId = keyPlayer_GroupId.get(keyPlayer);
                            LOGGER.error(groupScoketPlayers.get(groupId));
                            synchronized (SlotLock.INSTANCE._getSlotLock(keyPlayer)) {
                                InGame.INSTANCE.process(opBsPacket.get(), groupScoketPlayers.get(groupId), keyPlayer);
                            }
                        }
                    } else {
                        IOSocket.send(socketPlayer, BaseOutPacketInstance.UNKNOW_REQUEST_AFTER_CONNECT);
                        return;
                    }
                }
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
            } finally {
                closeConect(in, out, socket, socketPlayer);
            }

        }
    }

    public static void removeConnectPlayer(String key) {
        try {
            String groupId = null;
            for (Map.Entry<String, GroupScoketPlayer> entry : groupScoketPlayers.entrySet()) {
                GroupScoketPlayer groupScoketPlayer = entry.getValue();
                for (Map.Entry<String, SocketPlayer> entry1 : groupScoketPlayer.getSocketPlayers().entrySet()) {
                    SocketPlayer socketPlayer = entry1.getValue();
                    if (socketPlayer.getKey().equals(key)) {
                        groupScoketPlayers.remove(entry.getKey());
                        groupId = groupScoketPlayer.getIdGroup();
                        break;
                    }
                }

                if (groupId != null) {
                    availablePlayerGroupMode.remove(groupId);
                    for (Map.Entry<String, SocketPlayer> en : groupScoketPlayer.getSocketPlayers().entrySet()) {
                        String keyPlayer = en.getKey();
                        if (!keyPlayer.equals(key)) {
                            IOSocket.send(en.getValue().getOut(), BaseOutPacketInstance.COMPETITER_DISCONETED);
                        }
                        keyPlayer_GroupId.remove(en.getKey());
                    }
                    break;

                }
            }

            for (SocketPlayer socketPlayer : availablePlayer) {
                if (socketPlayer.getKey().equals(key)) {
                    availablePlayer.remove(socketPlayer);
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    public static void closeConect(Scanner in, PrintWriter out, Socket socket, SocketPlayer socketPlayer) {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (!socket.isClosed()) {
                socket.close();
            }
            removeConnectPlayer(socketPlayer.getKey());

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        LOGGER.info("availablePlayer" + availablePlayer.toString());

        LOGGER.info("Close connect of User: " + socketPlayer.getKey());

    }

    public static ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

    public static void main(String[] args) throws IOException {
        ses.scheduleWithFixedDelay(() -> {
            if (!availablePlayer.isEmpty()) {
                System.err.println(availablePlayer.get(0).getUserName());
                long waitTime = System.currentTimeMillis() - availablePlayer.get(0).getCreatedTime();
                if (waitTime > 5000) {
                    BotGame.startBoot();
                }
            }
        }, 5, 2, TimeUnit.SECONDS);
        LOGGER.info("The chat server is running...8080");
        ExecutorService pool = Executors.newFixedThreadPool(20);
        try (ServerSocket listener = new ServerSocket(8080)) {

            while (true) {
                pool.execute(new Handler(listener.accept()));
            }
        }

    }

}
