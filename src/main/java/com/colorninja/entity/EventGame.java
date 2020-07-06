/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author namhcn
 */
@Getter
@Setter
public class EventGame {

    private String eventId;
    private String eventname;
    private String backgroundUrl;
    private String description;
    private long createTime;

    public EventGame() {
        long curentTime = System.currentTimeMillis();
        this.eventId = curentTime + "";
        this.createTime = curentTime;
    }

    public EventGame(String eventname, String backgroundUrl, String description) {
        this.eventname = eventname;
        this.backgroundUrl = backgroundUrl;
        this.description = description;
        long curentTime = System.currentTimeMillis();
        this.eventId = curentTime + "";
        this.createTime = curentTime;
    }

}
