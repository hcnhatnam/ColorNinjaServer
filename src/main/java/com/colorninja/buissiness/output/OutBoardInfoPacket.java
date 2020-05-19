/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.buissiness.output;


import com.colorninja.buissiness.output.BaseOutPacket;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author namhcn
 */
@Getter
@Setter
public class OutBoardInfoPacket extends BaseOutPacket {


    protected String idGroup;
    protected Map<String, String> key_usernames;

    public OutBoardInfoPacket(String idRoom, Map<String, String> key_usernames) {
        super(TYPE_BOARD_INFO);
        this.idGroup = idRoom;
        this.key_usernames = key_usernames;
    }

}
