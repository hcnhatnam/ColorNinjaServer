/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.input;

import com.colorninja.entity.Utils;
import com.google.gson.JsonObject;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author namhcn
 */
@Getter
@Setter
public class BaseInPacket implements Serializable {

    public enum EInType {
        UNKKNOW, WIN, CLOSE, GET_KEY, LOOSE, STOP_ROUND, GET_KEY_GROUP_MODE, RE_MATCH, RE_MATCH_START_GAME;
    }

    public static EInType get(String input) {
        JsonObject jsonObject = Utils.gson.fromJson(input, JsonObject.class);
        String type = jsonObject.get("type").getAsString().toLowerCase();
        if (type.equals("win")) {
            return EInType.WIN;
        } else if (type.equals("get_key")) {
            return EInType.GET_KEY;
        } else if (type.equals("get_key_group_mode")) {
            return EInType.GET_KEY;
        } else if (type.equals("loose")) {
            return EInType.LOOSE;
        } else if (type.equals("close")) {
            return EInType.CLOSE;
        } else if (type.equals("stop_round")) {
            return EInType.STOP_ROUND;
        } else if (type.equals("re_match")) {
            return EInType.RE_MATCH;
        } else if (type.equals("re_match_start_game")) {
            return EInType.RE_MATCH_START_GAME;
        }
        return EInType.UNKKNOW;
    }
    protected int type;

    public BaseInPacket(EInType eInType) {
        int typeTmp = -1;
        switch (eInType) {
            case WIN:
                typeTmp = 0;
                break;
            case CLOSE:
                typeTmp = 1;
                break;
            case GET_KEY:
                typeTmp = 2;
                break;
            case LOOSE:
                typeTmp = 3;
                break;
            case STOP_ROUND:
                typeTmp = 4;
                break;
            case GET_KEY_GROUP_MODE:
                typeTmp = 7;
                break;
            case RE_MATCH:
                typeTmp = 8;
                break;
            case RE_MATCH_START_GAME:
                typeTmp = 9;
                break;
            case UNKKNOW:
                typeTmp = -1;
                break;
        }
        this.type = typeTmp;
    }

    public EInType getEType() {
        switch (type) {
            case 0:
                return EInType.WIN;
            case 1:
                return EInType.CLOSE;
            case 2:
                return EInType.GET_KEY;
            case 3:
                return EInType.LOOSE;
            case 4:
                return EInType.STOP_ROUND;
            case 7:
                return EInType.GET_KEY_GROUP_MODE;
            case 8:
                return EInType.RE_MATCH;
            case 9:
                return EInType.RE_MATCH_START_GAME;
            default:
                return EInType.UNKKNOW;
        }
    }

    @Override
    public String toString() {
        return Utils.gson.toJson(this);
    }

}
