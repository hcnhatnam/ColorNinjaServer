/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.input;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author namhcn
 */
@Getter
@Setter
public class InGamePacket extends BaseInPacket {

    protected int round;

    public InGamePacket(int round) {
        super(BaseInPacket.EInType.WIN);
        this.round = round;
    }
}
