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
import com.server.entity.HReqParam;
import com.database.LeaderBoard;
import com.server.entity.ResultObject;
import com.server.entity.ResultObjectInstance;
import com.server.entity.ScoreUser;
import com.server.model.BaseModel;
import java.io.IOException;
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
            try {
                String key = HReqParam.getString(req, "key");
                String username = HReqParam.getString(req, "username");
                String avatar = HReqParam.getString(req, "avatar");
                if (key.isEmpty() || username.isEmpty()) {
                    resultObject = ResultObjectInstance.MISSING_PARAM;
                } else {
                    Optional<ScoreUser> op = LeaderBoard.INSTANCE.get(key);
                    if (!op.isPresent()) {
                        ScoreUser scoreUser = new ScoreUser(key, username, avatar, 0, 0, 0, System.currentTimeMillis());
                        LeaderBoard.INSTANCE.insert(scoreUser);
                        LOGGER.info(scoreUser);
                    } else {
                        resultObject = ResultObjectInstance.USER_EXSISTED;
                    }
                }
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage(), ex);
                resultObject = ResultObjectInstance.MISSING_PARAM;
            }

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            resultObject = ResultObjectInstance.EXCEPTION;
        }

        returnJSon(resp, resultObject.toString());
    }
}
