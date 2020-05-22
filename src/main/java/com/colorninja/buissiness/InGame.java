/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.buissiness;

import com.colorninja.entity.GroupScoketPlayer;
import com.colorninja.buissiness.output.BaseOutPacketInstance;
import com.colorninja.entity.SocketPlayer;
import com.colorninja.input.BaseInPacket;
import com.colorninja.buissiness.output.BaseOutPacket;
import com.colorninja.input.InGamePacket;
import com.colorninja.buissiness.output.OutNewBoardPacket;
import com.colorninja.buissiness.output.OutNewBoardPacket.PREVIOUS_STATE;
import com.colorninja.buissiness.output.OutWinGamePacket;
import com.colorninja.buissiness.output.OutWinGamePacket.ScorePlayer;
import com.colorninja.entity.Utils;
import com.colorninja.server.SocketGameServer;
import java.util.ArrayList;
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
//            String log = "";
//            for (Map.Entry<String, SocketPlayer> entry : groupScoketPlayer.getSocketPlayers().entrySet()) {
//                SocketPlayer socketPlayer = entry.getValue();
//                log += String.format("%s_%s", socketPlayer.getUserName(), socketPlayer.getScore());
//            }
//            LOGGER.info(String.format("%s_%s", groupScoketPlayer.getIdGroup(), log));

            Map<String, SocketPlayer> mSo = groupScoketPlayer.getSocketPlayers();
            SocketPlayer curentPlayer = mSo.get(keyPlayer);
            if (packet.getEType() == BaseInPacket.EInType.WIN) {
                InGamePacket inGamePacket = (InGamePacket) packet;
                if (inGamePacket.getRound() != groupScoketPlayer.getRound()) {
                    IOSocket.send(curentPlayer, BaseOutPacketInstance.ROUND_EXPIRED);
                    return;
                }
                int currentRount = groupScoketPlayer.getRound();
                if (currentRount == MAX_WIN_NUMROUND) {
                    curentPlayer.setScore(curentPlayer.getScore() + 1);
                    winGame(mSo);
                } else if (currentRount < MAX_WIN_NUMROUND) {
                    int nextRound = setNewRound(groupScoketPlayer);
                    Map<PREVIOUS_STATE, OutNewBoardPacket> mOut = OutNewBoardPacket.getInstances(nextRound);
                    Map<String, BaseOutPacket> baseOutPackets = genMapWinOutputBroacast(keyPlayer, mSo, mOut.get(PREVIOUS_STATE.WIN), mOut.get(PREVIOUS_STATE.LOOSE));
                    IOSocket.broadcast(mSo.values(), baseOutPackets);
                } else {
                    IOSocket.send(curentPlayer, BaseOutPacketInstance.ROUND_EXCEED);
                }
            } else if (packet.getEType() == BaseInPacket.EInType.LOOSE) {
                InGamePacket inGamePacket = (InGamePacket) packet;
                if (inGamePacket.getRound() != groupScoketPlayer.getRound()) {
                    IOSocket.send(curentPlayer, BaseOutPacketInstance.ROUND_EXPIRED);
                    return;
                }
                int nextRound = setNewRound(groupScoketPlayer);
                Map<String, BaseOutPacket> baseOutPackets = new HashMap<>();
                Map<PREVIOUS_STATE, OutNewBoardPacket> mOut = OutNewBoardPacket.getInstances(nextRound);
                for (Map.Entry<String, SocketPlayer> entry : mSo.entrySet()) {
                    String key = entry.getKey();
                    SocketPlayer val = entry.getValue();
                    if (!key.equals(curentPlayer.getKey())) {
                        val.setScore(val.getScore() + 1);
                        baseOutPackets.put(key, mOut.get(PREVIOUS_STATE.WIN));
                    } else {
                        baseOutPackets.put(key, mOut.get(PREVIOUS_STATE.LOOSE));
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

    public Map<String, BaseOutPacket> genMapWinOutputBroacast(String keyWin, Map<String, SocketPlayer> mSo, OutNewBoardPacket winOuput, OutNewBoardPacket looseOuput) {
        Map<String, BaseOutPacket> baseOutPackets = new HashMap<>();

        for (Map.Entry<String, SocketPlayer> entry : mSo.entrySet()) {
            String key = entry.getKey();
            SocketPlayer val = entry.getValue();
            if (key.equals(keyWin)) {
                val.setScore(val.getScore() + 1);
                baseOutPackets.put(key, winOuput);
            } else {
                baseOutPackets.put(key, looseOuput);
            }
        }
        return baseOutPackets;
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
