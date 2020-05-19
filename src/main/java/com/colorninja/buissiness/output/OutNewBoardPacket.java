/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.buissiness.output;

import com.colorninja.buissiness.output.BaseOutPacket;
import com.colorninja.entity.BoardGame;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author namhcn
 */
@Getter
@Setter
public class OutNewBoardPacket extends BaseOutPacket {


    protected BoardGame boardGame;
    protected boolean isPreviousWinner;

    private OutNewBoardPacket(BoardGame boardGame) {
        super(TYPE_NEW_BOARD);
        this.boardGame = boardGame;
    }

    private OutNewBoardPacket(BoardGame boardGame, boolean isPreviosWinner) {
        this(boardGame);
        this.isPreviousWinner = isPreviosWinner;
    }

    public enum PREVIOUS_STATE {
        WIN, LOOSE;
    }

    public static Map<PREVIOUS_STATE, OutNewBoardPacket> getInstances(int round) {
        Map<PREVIOUS_STATE, OutNewBoardPacket> mRet = new HashMap<>();
        BoardGame boardGame = new BoardGame(round);
        OutNewBoardPacket win = new OutNewBoardPacket(boardGame, true);
        OutNewBoardPacket loose = new OutNewBoardPacket(boardGame, false);
        mRet.put(PREVIOUS_STATE.WIN, win);
        mRet.put(PREVIOUS_STATE.LOOSE, loose);
        return mRet;
    }
}
