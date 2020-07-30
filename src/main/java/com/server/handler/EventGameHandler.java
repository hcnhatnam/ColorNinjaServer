/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.handler;

/**
 *
 * @author namhcn
 */
import com.colorninja.entity.EventGame;
import com.database.EventGameDB;
import com.server.entity.ResultObject;
import com.server.entity.ResultObjectInstance;
import com.server.model.BaseModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class EventGameHandler extends BaseModel {

    private static final Logger LOGGER = Logger.getLogger(EventGameHandler.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResultObject resultObject = new ResultObject(0, "");
        try {
            List<EventGame> eventGames = EventGameDB.INSTANCE.get();
            if (eventGames == null && !eventGames.isEmpty()) {
                eventGames = new ArrayList<>();
            }
            resultObject.putData("eventgames", eventGames);

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            resultObject = ResultObjectInstance.EXCEPTION;
        }
        returnJSon(resp, resultObject.toString());
    }

}
