/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.buissiness.output;

import com.colorninja.entity.Utils;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author namhcn
 */
@Getter
@Setter
public class BaseOutPacket implements Serializable {

    public static int COMPETITER_DISCONETED = -11;
    public static int ROUND_EXCEED = -10;
    public static int GROUP_NOT_EXIST = -9;

    public static int UNKNOW_REQUEST_AFTER_CONNECT = -6;
    public static int UNKNOW_REQUEST = -5;
    public static int EXCEED_ROUND_CURRENT = -4;
    public static int ROUND_EXPIRED = -3;
    public static int PLAYER_KEY_EXISTED = -2;
    public static int ERROR_KEY = -1;
    public static int WAITING_PLAYER = 1;
    public static int REQUIRE_KEY = 2;
    public static int TYPE_WINGAME = 4;
    public static int TYPE_NEW_BOARD = 5;
    public static int TYPE_BOARD_INFO = 6;
    public static int TYPE_WAITING_GROUP = 7;
    public static int TYPE_REMATCH = 8;

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
