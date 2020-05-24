/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.colorninja.buissiness.output;

import com.server.entity.LeaderBoard;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author namhcn
 */
@Getter
@Setter
public class OutBoardInfoPacket extends BaseOutPacket {

    protected String idGroup;
    protected Map<String, String> key_usernames;
    protected Map<String, LeaderBoard.ScoreUser> key_avatars;

    public OutBoardInfoPacket(String idRoom, Map<String, String> key_usernames) {
        super(TYPE_BOARD_INFO);
        this.idGroup = idRoom;
        this.key_usernames = key_usernames;
        key_avatars = new HashMap<>();
        for (String key : key_usernames.keySet()) {
            Optional<LeaderBoard.ScoreUser> op = LeaderBoard.INSTANCE.getUserScore(key);
            if (op.isPresent()) {
                key_avatars.put(key, op.get());
            } else {
                key_avatars.put(key, null);
            }
        }
    }

}
