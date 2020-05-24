/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author namhcn
 */
@Getter
@Setter
public class KeyPlayerGroupModePacket extends KeyPlayerPacket {

    private String groupId;

    public KeyPlayerGroupModePacket(String keyPlayer, String groupId) {
        super(EInType.GET_KEY_GROUP_MODE, keyPlayer);
        this.groupId = groupId;

    }
}
