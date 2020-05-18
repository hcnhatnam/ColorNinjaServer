/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.objectingame;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author namhcn
 */
@Getter
@Setter
public class KeyPlayerPacket extends BaseInPacket {

    protected String keyPlayer;
    protected String username;

    public KeyPlayerPacket(String keyPlayer) {
        super(EInType.GET_KEY);
        this.keyPlayer = keyPlayer;
    }
}
