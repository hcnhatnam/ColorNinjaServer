/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.entity;

import com.colorninja.entity.Utils;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;
import com.server.model.MongoDb;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author namhcn
 */
public class LeaderBoard {

    public static LeaderBoard INSTANCE = new LeaderBoard();
    public MongoCollection _collection;

    private LeaderBoard() {
        _collection = MongoDb.INSTANCE.getleaderBoardDB().getCollection("leaderboard");
    }

    @Getter
    @Setter
    public static class ScoreUser {

        private String key;
        private String username;
        private String avatar;

        private int bestscore;
        private int numWinGame;
        private int numLooseGame;
        private long createTime;

        public ScoreUser(String key, String username, int maxScore, int numWinGame, int numLooseGame) {
            this.key = key;
            this.username = username;
            this.bestscore = maxScore;
            this.numWinGame = numWinGame;
            this.numLooseGame = numLooseGame;
            this.createTime = System.currentTimeMillis();
        }

        public ScoreUser(String key, String username, String avata, int maxScore, int numWinGame, int numLooseGame) {
            this.key = key;
            this.username = username;
            this.bestscore = maxScore;
            this.numWinGame = numWinGame;
            this.numLooseGame = numLooseGame;
            this.createTime = System.currentTimeMillis();
        }

        public Document getDocument() {
            Document document = new Document("key", this.key)
                    .append("username", this.username)
                    .append("avata", this.avatar)
                    .append("bestscore", this.bestscore)
                    .append("numWinGame", this.numWinGame)
                    .append("numLooseGame", this.numLooseGame);
            return document;
        }
    }

    private List<ScoreUser> getLeaderBoardAll() {
        List<ScoreUser> scoreUsers = new ArrayList<>();
        try (MongoCursor<Document> cursor = _collection.find().iterator()) {
            while (cursor.hasNext()) {
                ScoreUser scoreUser = Utils.gson.fromJson(cursor.next().toJson(), ScoreUser.class);
                scoreUsers.add(scoreUser);
            }
        }
        return scoreUsers;
    }

    public List<ScoreUser> getLeaderBoardSolo() {
        List<ScoreUser> scoreUsers = getLeaderBoardAll();
        scoreUsers.sort((o1, o2) -> {
            return o2.getNumWinGame() - o1.getNumWinGame();
        });
        return scoreUsers;
    }

    public List<ScoreUser> getLeaderBoard() {
        List<ScoreUser> scoreUsers = getLeaderBoardAll();
        scoreUsers.sort((o1, o2) -> {
            return o2.getBestscore() - o1.getBestscore();
        });
        return scoreUsers;
    }

    public Optional<ScoreUser> getUserScore(String keyUser) {

        try (MongoCursor<Document> cursor = _collection.find(eq("key", keyUser)).limit(1).iterator()) {
            while (cursor.hasNext()) {
                ScoreUser scoreUser = Utils.gson.fromJson(cursor.next().toJson(), ScoreUser.class);
                return Optional.ofNullable(scoreUser);
            }
        }
        return Optional.empty();
    }

    public void insertOrUpdate(ScoreUser scoreUser) {
        _collection.findOneAndDelete(eq("username", scoreUser.getUsername()));
        _collection.insertOne(scoreUser.getDocument());
    }

    public static void main(String[] args) {
        System.err.println(INSTANCE.getUserScore("D9CCB221-1D0A-4DAA-A3AA-93ADFC383CD9"));
//        INSTANCE.insertOrUpdate(new ScoreUser(1 + "", "namhcn", "http://avata.com", 1, 10, 5));
//        System.err.println(Utils.gson.toJson(INSTANCE.getLeaderBoard()));
    }
}
