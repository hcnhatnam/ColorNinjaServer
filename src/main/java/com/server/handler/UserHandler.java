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
import com.server.entity.LeaderBoard;
import com.server.entity.LeaderBoard.ScoreUser;
import com.server.entity.ResultObject;
import com.server.model.BaseModel;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class UserHandler extends BaseModel {

    private static final Logger LOGGER = Logger.getLogger(UserHandler.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResultObject resultObject = new ResultObject(0, "");

        try {
            String type = HReqParam.getString(req, "type");
            String key = HReqParam.getString(req, "key");
            if (key.isEmpty()) {
                resultObject.setError(ResultObject.ERROR);
                resultObject.setMessage("key is Empty");
            } else {
                Optional<ScoreUser> op = LeaderBoard.INSTANCE.getUserScore(key);
                if (op.isPresent()) {
                    ScoreUser scoreUser = op.get();
                    boolean isUpdate = false;
                    if (type.equals("username")) {
                        String username = HReqParam.getString(req, "username");
                        if (!username.isEmpty()) {
                            scoreUser.setUsername(username);
                            isUpdate = true;
                        } else {
                            resultObject.setError(ResultObject.ERROR);
                            resultObject.setMessage("username is Empty");
                        }
                    } else if (type.equals("avatar")) {
                        String avatar = HReqParam.getString(req, "avatar");
                        if (!avatar.isEmpty()) {
                            scoreUser.setAvatar(avatar);
                            isUpdate = true;
                        } else {
                            resultObject.setError(ResultObject.ERROR);
                            resultObject.setMessage("avatar is Empty");
                        }

                    } else if (type.equals("bestscore")) {
                        int bestScore = -1;
                        try {
                            bestScore = Integer.parseInt(HReqParam.getString(req, "bestscore"));
                        } catch (Exception e) {
                        }
                        if (bestScore != -1) {
                            if (scoreUser.getBestscore() < bestScore) {
                                scoreUser.setBestscore(bestScore);
                                isUpdate = true;
                            }
                        } else {
                            resultObject.setError(ResultObject.ERROR);
                            resultObject.setMessage("bestScore is Empty");
                        }
                    }
                    if (isUpdate) {
                        LeaderBoard.INSTANCE.insertOrUpdate(scoreUser);
                    }
                    LOGGER.info(scoreUser);
                } else {
                    resultObject.setError(ResultObject.ERROR);
                    resultObject.setMessage("user isn't exist");
                }

            }

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            resultObject.setError(ResultObject.ERROR);
            resultObject.setMessage(ex.getMessage());
        }

        returnJSon(resp, resultObject.toString());
    }
}
