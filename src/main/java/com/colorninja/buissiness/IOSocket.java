/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.buissiness;

import com.colorninja.entity.SocketPlayer;
import com.colorninja.entity.Utils;
import com.colorninja.objectingame.BaseInPacket;
import com.colorninja.objectingame.BaseOutPacket;
import com.colorninja.objectingame.InGamePacket;
import com.colorninja.objectingame.KeyPlayerPacket;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import javafx.util.Pair;
import org.apache.log4j.Logger;

/**
 *
 * @author namhcn
 */
public class IOSocket {

    private static final Logger LOGGER = Logger.getLogger(IOSocket.class);

    public static int send(SocketPlayer socketPlayer, BaseOutPacket outPacket) {
        try {
            LOGGER.info(String.format("sendToUser %s: %s", socketPlayer.getKey(), outPacket.toString()));
//            send(socketPlayer.getOut(), outPacket);
            socketPlayer.getOut().println(outPacket.toString());
            return 0;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return -1;
    }

    public static int send(PrintWriter objectOutputStream, BaseOutPacket outPacket) {
        try {
            LOGGER.info(String.format("sendToUnknowUser: %s", outPacket.toString()));
            objectOutputStream.println(outPacket.toString());
            return 0;

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return -1;
    }

    public static Optional<BaseInPacket> reciver(Scanner objectInputStream) {
        try {
            String json = objectInputStream.next();
            BaseInPacket object = Utils.gson.fromJson(json, BaseInPacket.class);
            if (object.getEType() == BaseInPacket.EInType.GET_KEY) {
                object = Utils.gson.fromJson(json, KeyPlayerPacket.class);
            } else if (object.getEType() == BaseInPacket.EInType.WIN) {
                object = Utils.gson.fromJson(json, InGamePacket.class);
            }
            LOGGER.info(String.format("reciverFromUnknowUser: %s", Utils.gson.toJson(object)));
            return Optional.ofNullable(object);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return Optional.empty();
    }

    public static int broadcast(Collection<SocketPlayer> socketPlayers, BaseOutPacket outPacket) {
        try {
            socketPlayers.parallelStream().forEach((SocketPlayer t) -> {
                try {
                    send(t, outPacket);
                } catch (Exception ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            });
            return 0;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return -1;
    }

    public static int broadcast(Collection<SocketPlayer> socketPlayers, Map<String, BaseOutPacket> outPackets) {
        try {
            socketPlayers.parallelStream().forEach((SocketPlayer t) -> {
                try {
                    send(t, outPackets.get(t.getKey()));
                } catch (Exception ex) {
                    LOGGER.error(ex.getMessage(), ex);
                }
            });
            return 0;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return -1;
    }

    public static void main(String[] args) {
        String s = "{type: 1, keyPlayer: \"124\"}";
        BaseInPacket k = Utils.gson.fromJson(s, KeyPlayerPacket.class);
        System.err.println(k.toString());
    }
}
