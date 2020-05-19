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
public class WaitingPlayerGroupModePacket extends BaseOutPacket {

    private static String messageWaitingGroupMode = "wating next player to join group";

    protected String idGroup;

    public WaitingPlayerGroupModePacket(String idGroup) {
        super(TYPE_WAITING_GROUP, messageWaitingGroupMode);
        this.idGroup = idGroup;
    }

}
