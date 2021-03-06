/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.entity;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author namhcn
 */
@Getter
@Setter
public class BoardGame {

    private static final int DELTA_COLOR_RANGE = 3;
    private int round;
    private int color;
    private int sizeBoard;
    private int index;
    private int secondColor;

    public BoardGame(int round) {
        this.round = round;
        this.color = Utils.getRandomColor();
        this.sizeBoard = Utils.MAX_POINT_IN_ROUND.get(round - 1);
        this.index = Utils.getRandom(sizeBoard * sizeBoard);
        this.secondColor = Utils.getRandom(DELTA_COLOR_RANGE);
    }
}
