/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.database;

import com.colorninja.server.SocketGameServer;
import com.server.entity.ScoreUser;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author namhcn
 */
public class LeaderBoard extends BaseDB<ScoreUser> {

    public static LeaderBoard INSTANCE = new LeaderBoard();

    private LeaderBoard() {
        super("leaderboard", ScoreUser.class);
    }

    public List<ScoreUser> getLeaderBoardSolo() {
        List<ScoreUser> scoreUsers = get();
        scoreUsers.sort((o1, o2) -> {
            return o2.getNumWinGame() - o1.getNumWinGame();
        });
        return scoreUsers;
    }

    public List<ScoreUser> getLeaderBoard() {
        List<ScoreUser> scoreUsers = get();
        scoreUsers.sort((o1, o2) -> {
            return o2.getBestscore() - o1.getBestscore();
        });
        return scoreUsers;
    }

    @Override
    protected String getKeyField() {
        return "key";
    }

    @Override
    protected String getKeyObj(ScoreUser obj) {
        return obj.getKey();
    }

    @Override
    public void update(ScoreUser obj) {
        obj.setLastUpdate(System.currentTimeMillis());
        super.update(obj);
    }

    public static void main(String[] args) {
//        System.err.println(INSTANCE.getUserScore("").get());
//        ScoreUser scoreUser = new ScoreUser("C383CD9", "namhcn2Test", "", 1, 1, 1, System.currentTimeMillis());
//        INSTANCE.insert(scoreUser);
        for (ScoreUser arg : INSTANCE.getLeaderBoard()) {
            System.err.println(arg.getCreateTime());

        }

//        System.err.println(Utils.gson.toJson(INSTANCE.getLeaderBoard()));
    }

}
