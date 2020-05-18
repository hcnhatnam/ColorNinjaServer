/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.objectingame;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author namhcn
 */
@Getter
@Setter
public class OutWinGamePacket extends BaseOutPacket {

    public static int TYPE_WINGAME = 4;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ScorePlayer {

        private String keyPlayer;
        private int score;

    }

    protected String keyWinner;
    protected int winnerscore;
    List<ScorePlayer> scorePlayers;

    public OutWinGamePacket(String keyWinner, int winnerscore, List<ScorePlayer> scorePlayers) {
        super(TYPE_WINGAME);
        this.keyWinner = keyWinner;
        this.winnerscore = winnerscore;
        this.scorePlayers = scorePlayers;
    }
}
