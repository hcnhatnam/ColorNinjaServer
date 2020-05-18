/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.objectingame;

import com.colorninja.Entity.Utils;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author namhcn
 */
@Getter
@Setter
public class BaseOutPacket implements Serializable{

    protected int type;
    protected String message;

    public BaseOutPacket(int type) {
        this(type, "");
    }

    public BaseOutPacket(int type, String message) {
        this.type = type;
        this.message = message;
    }

    @Override
    public String toString() {
        return Utils.gson.toJson(this);
    }
    

}
