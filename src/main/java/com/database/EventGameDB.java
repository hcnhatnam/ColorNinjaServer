/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.database;

import com.colorninja.entity.EventGame;
import java.util.List;

/**
 *
 * @author namhcn
 */
public class EventGameDB extends BaseDB<EventGame> {

    public static EventGameDB INSTANCE = new EventGameDB();

    private EventGameDB() {
        super("eventgame", EventGame.class);
    }

    @Override
    public List<EventGame> get() {
        List<EventGame> eventGames = super.get();
        eventGames.sort((o1, o2) -> {
            return (int) (o2.getCreateTime() - o1.getCreateTime());
        });
        return eventGames;
    }

    @Override
    protected String getKeyField() {
        return "eventid";
    }

    @Override
    protected String getKeyObj(EventGame obj) {
        return obj.getEventId();
    }

    public static void main(String[] args) {
        System.err.println(INSTANCE.get().get(0).getEventname());
//        INSTANCE.insert(new EventGame("eventtest2", "https://b.f1.photo.talk.zdn.vn/8254651986745831964/23db24d85817a549fc06.jpg", "test nhe"));
    }

}
