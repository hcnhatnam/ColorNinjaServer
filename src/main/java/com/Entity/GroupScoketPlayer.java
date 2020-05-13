/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author namhcn
 */
@Getter
@Setter
public class GroupScoketPlayer {

    private String idGroup;
    private int round;
    private Map<String, SocketPlayer> socketPlayers;

    private GroupScoketPlayer() {

    }

    public GroupScoketPlayer(SocketPlayer player1, SocketPlayer player2) {
        this.idGroup = player1.getKey() + "_" + player2.getKey();
        this.socketPlayers = new ConcurrentHashMap<>();
        this.socketPlayers.put(player1.getKey(), player1);
        this.socketPlayers.put(player2.getKey(), player2);
        this.round = 1;
    }

    public void addPlayer(SocketPlayer socketPlayer) {
        socketPlayers.put(socketPlayer.getKey(), socketPlayer);
    }
}
