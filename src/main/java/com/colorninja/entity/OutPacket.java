/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.entity;

import com.colorninja.objectingame.BaseOutPacket;

/**
 *
 * @author namhcn
 */
public class OutPacket {

    public static BaseOutPacket REQUIRE_KEY = new BaseOutPacket(2, "require key");
    public static BaseOutPacket ERROR_KEY = new BaseOutPacket(-1, "error key");
    public static BaseOutPacket UNKNOW_REQUEST = new BaseOutPacket(-5, "unknow request");
    public static BaseOutPacket UNKNOW_REQUEST_AFTER_CONNECT = new BaseOutPacket(-6, "unknow request after connect");
    public static BaseOutPacket PLAYER_KEY_EXISTED = new BaseOutPacket(-2, "player key existed");
    public static BaseOutPacket WAITING_PLAYER = new BaseOutPacket(1, "wating next player");
    public static BaseOutPacket ROUND_EXPIRED = new BaseOutPacket(-3, "round expired");
    public static BaseOutPacket ROUND_EXCEED= new BaseOutPacket(-10, "exceed max round");

//    public static ResultObject getResultObjectBoardGame(int currentBoard) {
//        ResultObject resultObject = new ResultObject("board_game", new BoardGame(currentBoard));
//        return resultObject;
//    }
//
//    public static ResultObject getResultObjectWinGame(String keyWinner, int score, JsonArray jsonArray) {
//        JsonObject js = new JsonObject();
//        js.addProperty("winner", keyWinner);
//        js.addProperty("winnerscore", score);
//        js.add("statistical", jsonArray);
//        ResultObject resultObject = new ResultObject("win_game", js);
//        return resultObject;
//    }
//
//    public static void main(String[] args) {
//        System.err.println(REQUIRE_KEY.toString());
//    }
}
