/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.model;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 *
 * @author namhcn
 */
public class MongoDbConnect {

    public static MongoDbConnect INSTANCE = new MongoDbConnect();
    private MongoDatabase _leaderboardDB;
    private MongoClient _mongoClient;

    private MongoDbConnect() {
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
        String connectionStr = "mongodb://ian:secretPassword@35.198.220.200:27017/?authSource=cool_db";
        ConnectionString connectionString = new ConnectionString(connectionStr);
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();
        _mongoClient = MongoClients.create(clientSettings);

        _leaderboardDB = _mongoClient.getDatabase("cool_db");
//        MongoCollection<Grade> grades = db.getCollection("grades", Grade.class);
//
//        // create a new grade.
//        Grade newGrade = new Grade().setStudent_id(10003d)
//                .setClass_id(10d)
//                .setScores(singletonList(new Score().setType("homework").setScore(50d)));
//        grades.insertOne(newGrade);
//
//        // find this grade.
//        Grade grade = grades.find(eq("student_id", 10003d)).first();
//        System.out.println("Grade found:\t" + grade);
//
//        // update this grade: adding an exam grade
//        List<Score> newScores = new ArrayList<>(grade.getScores());
//        newScores.add(new Score().setType("exam").setScore(42d));
//        grade.setScores(newScores);
//        Document filterByGradeId = new Document("_id", grade.getId());
//        FindOneAndReplaceOptions returnDocAfterReplace = new FindOneAndReplaceOptions().returnDocument(ReturnDocument.AFTER);
//        Grade updatedGrade = grades.findOneAndReplace(filterByGradeId, grade, returnDocAfterReplace);
//        System.out.println("Grade replaced:\t" + updatedGrade);
//
//        // delete this grade
//        System.out.println(grades.deleteOne(filterByGradeId));

    }

    public MongoDatabase getleaderBoardDB() {
        return _leaderboardDB;
    }

    public static void main(String[] args) {
        System.err.println(System.currentTimeMillis());
    }
}
