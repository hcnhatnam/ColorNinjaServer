/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Entity;

import com.google.gson.JsonObject;

/**
 *
 * @author namhcn
 */
public class TypeInput {

    public enum ETypeInput {
        UNKKNOW, WIN, CLOSE, GET_KEY
    }

    public static ETypeInput get(String input) {
        JsonObject jsonObject = Utils.gson.fromJson(input, JsonObject.class);
        String type = jsonObject.get("type").getAsString().toLowerCase();
        if (type.equals("win")) {
            return ETypeInput.WIN;
        } else if (type.equals("get_key")) {
            return ETypeInput.GET_KEY;
        } else if (type.equals("close")) {
            return ETypeInput.CLOSE;
        }
        return ETypeInput.UNKKNOW;
    }
    
}
