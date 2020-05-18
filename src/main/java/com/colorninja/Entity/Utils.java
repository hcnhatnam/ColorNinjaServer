/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.Entity;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author namhcn
 */
public class Utils {

    public static final Gson gson = new Gson();
    public static Random _randomColor = new Random();
    public static int MAX_COLOR = 20;
    public static List<Integer> MAX_POINT_IN_ROUND = new ArrayList<>();
//    1->5=2x2;
//        1->5=2x2;

    static {
        for (int i = 1; i <= 5 * 6; i += 5) {
            int size = i / 5 + 2;
            for (int j = 0; j < 5; j++) {
                MAX_POINT_IN_ROUND.add(size);
            }
        }
    }

    public Utils() {
    }

    public static int getRandom(int max) {
        return _randomColor.nextInt(max);
    }

    public static int getRandomColor() {
        return _randomColor.nextInt(MAX_COLOR);
    }

    public static void main(String[] args) {
        System.err.println("MAX_POINT_IN_ROUND:" + MAX_POINT_IN_ROUND);
    }
}