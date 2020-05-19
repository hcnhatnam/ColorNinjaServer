/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.entity;

import com.google.gson.JsonObject;
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
public class ResultObject {

    private String type;
    private Object data;

    public ResultObject(String type, String message) {
        this.type = type;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("message", message);
        this.data = jsonObject;
    }

    @Override
    public String toString() {
        return Utils.gson.toJson(this);
    }

}
