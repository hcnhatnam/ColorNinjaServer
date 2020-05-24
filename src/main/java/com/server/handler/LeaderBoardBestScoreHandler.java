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
import com.server.entity.LeaderBoard;
import com.server.entity.LeaderBoard.ScoreUser;
import com.server.entity.ResultObject;
import com.server.model.BaseModel;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class LeaderBoardBestScoreHandler extends BaseModel {

    private static final Logger LOGGER = Logger.getLogger(LeaderBoardBestScoreHandler.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResultObject resultObject = new ResultObject(0, "");

        try {
            List<ScoreUser> scoreUsers = LeaderBoard.INSTANCE.getLeaderBoard();
            resultObject.putData("scoreusers", scoreUsers);
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
