/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.eq;
import com.server.entity.ScoreUser;
import com.server.model.MongoDbConnect;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.log4j.Logger;

/**
 *
 * @author namhcn
 * @param <T>
 */
public abstract class BaseDB<T> {

    public MongoCollection<T> _collection;
    private static int NUM_RETRY = 10;
    private static final Logger LOGGER = Logger.getLogger(BaseDB.class);

    public BaseDB(String collectionName, Class clazz) {
        _collection = MongoDbConnect.INSTANCE.getleaderBoardDB().getCollection(collectionName, clazz);
    }

    protected abstract String getKeyField();

    protected abstract String getKeyObj(T obj);

    public List<T> get() {
        List<T> list = new ArrayList<>();
        try (MongoCursor<T> cursor = _collection.find().iterator()) {
            while (cursor.hasNext()) {
                list.add(cursor.next());
            }
        }
        return list;
    }

    public Optional<T> get(String key) {
        for (int i = 0; i < NUM_RETRY; i++) {
            try (MongoCursor<T> cursor = _collection.find(eq(getKeyField(), key)).iterator()) {
                while (cursor.hasNext()) {
                    return Optional.ofNullable(cursor.next());
                }
            }
            LOGGER.error("Not Found:"+key);
        }
        return Optional.empty();
    }

    public void insert(T obj) {
        if (!get(getKeyObj(obj)).isPresent()) {
            _collection.insertOne(obj);
        }
    }

    public void update(T obj) {
        _collection.replaceOne(eq(getKeyField(), getKeyObj(obj)), obj);
    }
}
