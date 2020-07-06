/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.Document;

/**
 *
 * @author namhcn
 */
@Getter
@Setter
@NoArgsConstructor
public class ScoreUser {

    private String key;
    private String username;
    private String avatar;

    private int bestscore;
    private int numWinGame;
    private int numLooseGame;
    private long createTime;
    private long lastUpdate;

    public ScoreUser(String key, String username, String avatar, int maxScore, int numWinGame, int numLooseGame, long createTime) {
        this.key = key;
        this.username = username;
        this.avatar = avatar;
        this.bestscore = maxScore;
        this.numWinGame = numWinGame;
        this.numLooseGame = numLooseGame;
        this.createTime = createTime;
    }

//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public void setAvatar(String avatar) {
//        this.avatar = avatar;
//    }
//
//    public void setBestscore(int bestscore) {
//        this.bestscore = bestscore;
//    }
//
//    public void setNumWinGame(int numWinGame) {
//        this.numWinGame = numWinGame;
//    }
//
//    public void setNumLooseGame(int numLooseGame) {
//        this.numLooseGame = numLooseGame;
//    }

}
