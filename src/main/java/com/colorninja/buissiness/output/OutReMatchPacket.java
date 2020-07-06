/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.buissiness.output;

import com.colorninja.entity.BoardGame;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author namhcn
 */
@Getter
@Setter
public class OutReMatchPacket extends BaseOutPacket {
    private String keyPlayerReMatch;
    private String usernameReMatch;

    public OutReMatchPacket(String keyPlayerReMatch,String usernameReMatch) {
        super(TYPE_REMATCH);
        this.usernameReMatch = usernameReMatch;
        this.keyPlayerReMatch=keyPlayerReMatch;
    }

}
