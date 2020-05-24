/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.server;

import com.colorninja.entity.Utils;
import com.colorninja.input.KeyPlayerGroupModePacket;

/**
 *
 * @author namhcn
 */
public class Test {

    public static void main(String[] args) {
        System.err.println(Utils.gson.toJson(new KeyPlayerGroupModePacket("2", "2")));
    }
}
