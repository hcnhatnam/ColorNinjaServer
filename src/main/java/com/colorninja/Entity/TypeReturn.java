/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.Entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
/**
 *
 * @author namhcn
 */
public class TypeReturn {

    public static ResultObject REQUIRE_KEY = new ResultObject("require_key", "require key");
    public static ResultObject ERROR_KEY = new ResultObject("error_key", "error key");
    public static ResultObject ERROR_TYPE = new ResultObject("error_type", "type unkbow");
    public static ResultObject PLAYER_KEY_EXISTED = new ResultObject("player_exist", "player key existed");
    public static ResultObject WAITING_PLAYER = new ResultObject("wating_player", "wating next player");
    public static ResultObject ROUND_EXPIRED = new ResultObject("round_expired", "round expired");

    public static ResultObject getResultObjectBoardGame(int currentBoard) {
        ResultObject resultObject = new ResultObject("board_game", new BoardGame(currentBoard));
        return resultObject;
    }

    public static ResultObject getResultObjectWinGame(String keyWinner, int score, JsonArray jsonArray) {
        JsonObject js = new JsonObject();
        js.addProperty("winner", keyWinner);
        js.addProperty("winnerscore", score);
        js.add("statistical", jsonArray);
        ResultObject resultObject = new ResultObject("win_game", js);
        return resultObject;
    }

    public static void main(String[] args) {
        System.err.println(REQUIRE_KEY.toString());
    }
}
