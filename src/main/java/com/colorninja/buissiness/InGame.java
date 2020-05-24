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
import com.colorninja.server.SocketGameServer;
import com.server.entity.LeaderBoard;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
                    IOSocket.send(curentPlayer, BaseOutPacketInstance.ROUND_EXPIRED);
                    return;
                }
                int currentRount = groupScoketPlayer.getRound();
                if (currentRount == MAX_WIN_NUMROUND) {
                    int nextRound = setNewRound(groupScoketPlayer);
                    curentPlayer.setScore(curentPlayer.getScore() + 1);
                    winGameAnSaveScore(mSo);
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
                winGameAnSaveScore(mSo);
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
        GroupScoketPlayer group = SocketGameServer.groupScoketPlayers.get(groupScoketPlayer.getIdGroup());
//        if (group != null) {
//            group.setRound(nextRound);
//
//        }
        return nextRound;
    }

    public void winGameAnSaveScore(Map<String, SocketPlayer> mSo) {
        try {

            OutWinGamePacket winPacket = genOutputWinGame(mSo);
            for (ScorePlayer socketPlayer : winPacket.getScorePlayers()) {
                Optional<LeaderBoard.ScoreUser> op = LeaderBoard.INSTANCE.getUserScore(socketPlayer.getKeyPlayer());
                LeaderBoard.ScoreUser scoreUser = null;
                if (socketPlayer.getScore() == winPacket.getWinnerscore()) {
                    if (op.isPresent()) {
                        scoreUser = op.get();
                        scoreUser.setNumWinGame(scoreUser.getNumWinGame() + 1);
                    } else {
                        SocketPlayer player = mSo.get(socketPlayer.getKeyPlayer());
                        scoreUser = new LeaderBoard.ScoreUser(player.getKey(),
                                player.getUserName(), 0, 1, 0);
                    }
                } else {
                    if (op.isPresent()) {
                        scoreUser = op.get();
                        scoreUser.setNumLooseGame(scoreUser.getNumWinGame() + 1);
                    } else {
                        SocketPlayer player = mSo.get(socketPlayer.getKeyPlayer());
                        scoreUser = new LeaderBoard.ScoreUser(player.getKey(),
                                player.getUserName(), 0, 0, 1);
                    }
                }

                LeaderBoard.INSTANCE.insertOrUpdate(scoreUser);
            }
            IOSocket.broadcast(mSo.values(), winPacket);

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    public OutWinGamePacket genOutputWinGame(Map<String, SocketPlayer> mSo) {
        try {

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
                System.err.println(entry.getKey() + "ddddddddddd");
            }
            OutWinGamePacket winPacket = new OutWinGamePacket(winnerKey, maxScore, scorePlayers);
            return winPacket;

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return null;
    }

    public void winGame(Map<String, SocketPlayer> mSo) {
        OutWinGamePacket winPacket = genOutputWinGame(mSo);
        IOSocket.broadcast(mSo.values(), winPacket);
    }
}
