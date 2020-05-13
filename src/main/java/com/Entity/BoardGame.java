/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author namhcn
 */
@Getter
@Setter
@AllArgsConstructor
public class BoardGame {

    private int round;
    private int color;

    public BoardGame(int round) {
        this.round = round;
        this.color = Utils.getRandomColor();
    }
}
