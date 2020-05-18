/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.buissiness;

import com.colorninja.Entity.GroupScoketPlayer;
import com.colorninja.Entity.OutPacket;
import com.colorninja.Entity.ResultObject;
import com.colorninja.Entity.SocketPlayer;
import com.colorninja.Entity.TypeInput;
import com.colorninja.Entity.TypeReturn;
import com.colorninja.Entity.Utils;
import com.colorninja.objectingame.BaseInPacket;
import com.colorninja.objectingame.BaseOutPacket;
import com.colorninja.objectingame.InGamePacket;
import com.colorninja.objectingame.KeyPlayerPacket;
import com.colorninja.objectingame.OutNewBoardPacket;
import com.colorninja.objectingame.OutWinGamePacket;
import com.colorninja.objectingame.OutWinGamePacket.ScorePlayer;
import com.colorninja.server.SocketGameServer;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author namhcn
 */
public class InGame {

    private static final Logger LOGGER = Logger.getLogger(SocketGameServer.class);
    private static final int MAX_WIN_NUMROUND = 30;
    public static InGame INSTANCE = new InGame();

    /**
     * *
     *
     * @param packet
     * @param groupScoketPlayer
     * @param keyPlayer
     */
    public void process(BaseInPacket packet, GroupScoketPlayer groupScoketPlayer, String keyPlayer) {
        try {
            Map<String, SocketPlayer> mSo = groupScoketPlayer.getSocketPlayers();
            SocketPlayer curentPlayer = mSo.get(keyPlayer);
            if (packet.getEType() == BaseInPacket.EInType.WIN) {
                InGamePacket inGamePacket = (InGamePacket) packet;
                if (inGamePacket.getRound() != groupScoketPlayer.getRound()) {
                    IOSocket.send(curentPlayer, OutPacket.ROUND_EXPIRED);
                    return;
                }
                curentPlayer.setScore(curentPlayer.getScore() + 1);
                int currentRount = groupScoketPlayer.getRound();
                if (currentRount == MAX_WIN_NUMROUND) {
                    winGame(mSo);
                } else if (currentRount < MAX_WIN_NUMROUND) {
                    int nextRound = setNewRound(groupScoketPlayer);
                    Map<String, BaseOutPacket> baseOutPackets = new HashMap<>();
                    for (Map.Entry<String, SocketPlayer> entry : mSo.entrySet()) {
                        String key = entry.getKey();
                        SocketPlayer val = entry.getValue();
                        if (key.equals(curentPlayer.getKey())) {
                            val.setScore(val.getScore() + 1);
                            baseOutPackets.put(key, OutNewBoardPacket.getOutNewBoardPacket(nextRound, true));
                        } else {
                            baseOutPackets.put(key, OutNewBoardPacket.getOutNewBoardPacket(nextRound, false));
                        }
                    }
                    IOSocket.broadcast(mSo.values(), baseOutPackets);
                } else {
                    IOSocket.send(curentPlayer, OutPacket.ROUND_EXCEED);
                }
            } else if (packet.getEType() == BaseInPacket.EInType.LOOSE) {
                int nextRound = setNewRound(groupScoketPlayer);
                Map<String, BaseOutPacket> baseOutPackets = new HashMap<>();
                for (Map.Entry<String, SocketPlayer> entry : mSo.entrySet()) {
                    String key = entry.getKey();
                    SocketPlayer val = entry.getValue();
                    if (!key.equals(curentPlayer.getKey())) {
                        val.setScore(val.getScore() + 1);
                        baseOutPackets.put(key, OutNewBoardPacket.getOutNewBoardPacket(nextRound, true));
                    }
                }
                IOSocket.broadcast(mSo.values(), baseOutPackets);
            } else if (packet.getEType() == BaseInPacket.EInType.STOP_ROUND) {
                winGame(mSo);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    public int setNewRound(GroupScoketPlayer groupScoketPlayer) {
        int currentRount = groupScoketPlayer.getRound();
        int nextRound = currentRount + 1;
        groupScoketPlayer.setRound(nextRound);
        return nextRound;
    }

    public void winGame(Map<String, SocketPlayer> mSo) {
        List<ScorePlayer> scorePlayers = new ArrayList<>();
        String winnerKey = "";
        int maxScore = 0;
        for (Map.Entry<String, SocketPlayer> entry : mSo.entrySet()) {
            String key = entry.getKey();
            SocketPlayer sk = entry.getValue();
            if (sk.getScore() > maxScore) {
                winnerKey = key;
                maxScore = sk.getScore();
            }
            ScorePlayer scorePlayer = new ScorePlayer(key, sk.getScore());
            scorePlayers.add(scorePlayer);
        }
        OutWinGamePacket winPacket = new OutWinGamePacket(winnerKey, maxScore, scorePlayers);
        IOSocket.broadcast(mSo.values(), winPacket);
    }
}
