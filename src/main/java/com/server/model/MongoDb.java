/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.model;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author namhcn
 */
public class MongoDb {

    public static MongoDb INSTANCE = new MongoDb();
    private MongoDatabase _leaderboardDB;
    private MongoClient _mongoClient;

    private MongoDb() {
        _mongoClient = new MongoClient("119.82.135.105", 27017);
        _leaderboardDB = _mongoClient.getDatabase("colorninja");
    }

    public MongoDatabase getleaderBoardDB() {
        return _leaderboardDB;
    }
}
