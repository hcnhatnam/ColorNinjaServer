/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.entity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
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
@AllArgsConstructor
@NoArgsConstructor
public class SocketPlayer {

    private String key;
    private String userName;
    private PrintWriter out;
    private Scanner in;
    private int score;
    private long createdTime;

    public SocketPlayer(String key, String userName, PrintWriter out, Scanner in, int score) {
        this.key = key;
        this.userName = userName;
        this.out = out;
        this.in = in;
        this.score = score;
        this.createdTime = System.currentTimeMillis();
    }

}
