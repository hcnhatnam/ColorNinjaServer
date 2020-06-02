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
import com.colorninja.entity.Utils;
import com.server.entity.HReqParam;
import com.server.entity.LeaderBoard;
import com.server.entity.LeaderBoard.ScoreUser;
import com.server.entity.ResultObject;
import com.server.model.BaseModel;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class RegisterUserHandler extends BaseModel {

    private static final Logger LOGGER = Logger.getLogger(RegisterUserHandler.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResultObject resultObject = new ResultObject(0, "");

        try {
            String key = "";
            String username = "";
            String avatar = "";
            try {
                key = HReqParam.getString(req, "key");
                username = HReqParam.getString(req, "username");
                avatar = HReqParam.getString(req, "avatar");
            } catch (Exception e) {
            }
            if (key.isEmpty()) {
                resultObject.setError(ResultObject.ERROR);
                resultObject.setMessage("key is empty");
            } else if (username.isEmpty()) {
                resultObject.setError(ResultObject.ERROR);
                resultObject.setMessage("username is empty");
            } //            else if (avatar.isEmpty()) {
            //                resultObject.setError(ResultObject.ERROR);
            //                resultObject.setMessage("avatar is empty");
            //            } 
            else {
                Optional<ScoreUser> op = LeaderBoard.INSTANCE.getUserScore(key);
                if (!op.isPresent()) {
                    ScoreUser scoreUser = new ScoreUser(key, username, avatar, 0, 0, 0);
                    LeaderBoard.INSTANCE.insert(scoreUser);
                    LOGGER.info(scoreUser);
                } else {
                    resultObject.setError(ResultObject.ERROR);
                    resultObject.setMessage("user existed");
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            resultObject.setError(ResultObject.ERROR);
            resultObject.setMessage(ex.getMessage());
        }

        returnJSon(resp, resultObject.toString());
    }
//    @Override
//    public void get(String target,
//            Request baseRequest,
//            HttpServletRequest request,
//            HttpServletResponse response) throws IOException,
//            ServletException {
//        response.setContentType("text/html; charset=utf-8");
//        response.setStatus(HttpServletResponse.SC_OK);
//
//        PrintWriter out = response.getWriter();
//
//        out.println("<h1>" + greeting + "</h1>");
//        if (body != null) {
//            out.println(body);
//        }
//
//        baseRequest.setHandled(true);
//    }
}
