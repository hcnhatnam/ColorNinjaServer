/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.objectingame;

import com.colorninja.Entity.BoardGame;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author namhcn
 */
@Getter
@Setter
public class OutNewBoardPacket extends BaseOutPacket {

    public static int TYPE_NEW_BOARD = 5;

    protected BoardGame boardGame;
    protected List<String> userNames;
    protected boolean isPreviosWinner;

    public OutNewBoardPacket(int round) {
        super(TYPE_NEW_BOARD);
        this.boardGame = new BoardGame(round);
    }

    public static OutNewBoardPacket getOutNewBoardPacket(int round, boolean isPreviosWinner) {
        OutNewBoardPacket outNewBoardPacket = new OutNewBoardPacket(round);
        outNewBoardPacket.isPreviosWinner = isPreviosWinner;
        return outNewBoardPacket;
    }

    public OutNewBoardPacket(int round, List<String> userNames) {
        this(round);
        this.userNames = userNames;
    }
}
