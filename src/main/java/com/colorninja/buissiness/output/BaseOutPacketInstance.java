/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.buissiness.output;

import com.colorninja.buissiness.output.BaseOutPacket;

/**
 *
 * @author namhcn
 */
public class BaseOutPacketInstance {

    public static BaseOutPacket REQUIRE_KEY = new BaseOutPacket(BaseOutPacket.REQUIRE_KEY, "require key");
    public static BaseOutPacket ERROR_KEY = new BaseOutPacket(BaseOutPacket.ERROR_KEY, "error key");
    public static BaseOutPacket UNKNOW_REQUEST = new BaseOutPacket(BaseOutPacket.UNKNOW_REQUEST, "unknow request");
    public static BaseOutPacket UNKNOW_REQUEST_AFTER_CONNECT = new BaseOutPacket(BaseOutPacket.UNKNOW_REQUEST_AFTER_CONNECT, "unknow request after connect");
    public static BaseOutPacket PLAYER_KEY_EXISTED = new BaseOutPacket(BaseOutPacket.PLAYER_KEY_EXISTED, "player key existed");
    public static BaseOutPacket WAITING_PLAYER = new BaseOutPacket(BaseOutPacket.WAITING_PLAYER, "wating next player");
    public static BaseOutPacket ROUND_EXPIRED = new BaseOutPacket(BaseOutPacket.ROUND_EXPIRED, "round expired");
    public static BaseOutPacket ROUND_EXCEED = new BaseOutPacket(BaseOutPacket.ROUND_EXCEED, "exceed max round");
    public static BaseOutPacket GROUP_NOT_EXIST = new BaseOutPacket(BaseOutPacket.GROUP_NOT_EXIST, "group not exist");
    public static BaseOutPacket COMPETITER_DISCONETED = new BaseOutPacket(BaseOutPacket.COMPETITER_DISCONETED, "competiter disconected");

}
