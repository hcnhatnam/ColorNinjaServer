/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.handler;

import com.colorninja.entity.ColorRGB;
import com.server.entity.ResultObject;
import com.server.entity.ResultObjectInstance;
import com.server.model.BaseModel;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author namhcn
 */
public class AppConfigHandler extends BaseModel {

    private static final Logger LOGGER = Logger.getLogger(EventGameHandler.class);
    public static final List<ColorRGB> colorRGB = new ArrayList<>();
    public static final ColorRGB homeBackgroundColor;
    public static final boolean isEnableAdvertisement;

    static {
        ColorRGB colorRGB1 = new ColorRGB(8, 170, 145);
        ColorRGB colorRGB2 = new ColorRGB(238, 93, 39);
        ColorRGB colorRGB3 = new ColorRGB(223, 27, 83);
        ColorRGB colorRGB4 = new ColorRGB(118, 41, 140);
        ColorRGB colorRGB5 = new ColorRGB(84, 79, 136);
        ColorRGB colorRGB6 = new ColorRGB(144, 11, 62);
        ColorRGB colorRGB7 = new ColorRGB(119, 1, 56);
        ColorRGB colorRGB8 = new ColorRGB(255, 87, 51);
        ColorRGB colorRGB9 = new ColorRGB(67, 41, 103);
        ColorRGB colorRGB10 = new ColorRGB(120, 45, 101);
        ColorRGB colorRGB11 = new ColorRGB(249, 66, 82);
        ColorRGB colorRGB12 = new ColorRGB(32, 68, 91);
        ColorRGB colorRGB13 = new ColorRGB(123, 56, 65);
        ColorRGB colorRGB14 = new ColorRGB(248, 231, 169);
        ColorRGB colorRGB15 = new ColorRGB(197, 225, 111);
        ColorRGB colorRGB16 = new ColorRGB(196, 55, 129);
        ColorRGB colorRGB17 = new ColorRGB(70, 93, 110);
        ColorRGB colorRGB18 = new ColorRGB(240, 192, 168);

        colorRGB.add(colorRGB1);
        colorRGB.add(colorRGB2);
        colorRGB.add(colorRGB3);
        colorRGB.add(colorRGB4);
        colorRGB.add(colorRGB5);
        colorRGB.add(colorRGB6);
        colorRGB.add(colorRGB7);
        colorRGB.add(colorRGB8);
        colorRGB.add(colorRGB9);
        colorRGB.add(colorRGB10);
        colorRGB.add(colorRGB11);
        colorRGB.add(colorRGB12);
        colorRGB.add(colorRGB13);
        colorRGB.add(colorRGB14);
        colorRGB.add(colorRGB15);
        colorRGB.add(colorRGB16);
        colorRGB.add(colorRGB17);
        colorRGB.add(colorRGB18);

        homeBackgroundColor = new ColorRGB(44, 44, 44);
        isEnableAdvertisement = true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        ResultObject resultObject = new ResultObject(0, "");
        try {
            resultObject.putData("listColors", colorRGB);
            resultObject.putData("homeBackgroundColor", homeBackgroundColor);
            resultObject.putData("isEnableAdvertisement", isEnableAdvertisement);

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            resultObject = ResultObjectInstance.EXCEPTION;
        }
        returnJSon(resp, resultObject.toString());

    }

}
