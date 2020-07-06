/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.model;

import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import java.util.Arrays;

/**
 *
 * @author namhcn
 */
public class MongoDb {

    public static MongoDb INSTANCE = new MongoDb();
    private MongoDatabase _leaderboardDB;
    private MongoClient _mongoClient;

//    private MongoDb() {
////        MongoCredential credential = MongoCredential.createCredential("namhcn", "cool_db", "11112222".toCharArray());
////        _mongoClient = new MongoClient(new ServerAddress("119.82.135.105", 27017), Arrays.asList(credential));
//        
//        
//        MongoCredential credential = MongoCredential.createCredential("ian", "cool_db", "secretPassword".toCharArray());
//        _mongoClient = new MongoClient(new ServerAddress("35.198.220.200", 27017), Arrays.asList(credential));
//
//        _leaderboardDB = _mongoClient.getDatabase("cool_db");
//    }
//
    public MongoDatabase getleaderBoardDB() {
        return _leaderboardDB;
    }

//    public static void main(String[] args) {
//        System.err.println(System.currentTimeMillis());
//    }
}
