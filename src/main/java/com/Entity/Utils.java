/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Entity;

import com.google.gson.Gson;
import java.util.Random;

/**
 *
 * @author namhcn
 */
public class Utils {
    public static final Gson gson = new Gson();
    public static Random _randomColor = new Random();
    public static int MAX_COLOR = 20;

    public static int getRandomColor() {
        return _randomColor.nextInt(MAX_COLOR);
    }
}
