/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Server;

import com.server.handler.LeaderBoardBestScoreHandler;
import com.server.handler.LeaderBoardHandler;
import com.server.handler.RegisterUserHandler;
import com.server.handler.UserHandler;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 *
 * @author namhcn
 */
public class HServer {

    public static Server createServer(int port) {
        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler();
        context.addServlet(LeaderBoardHandler.class, "/leaderboard");
        context.addServlet(LeaderBoardBestScoreHandler.class, "/leaderboard/bestscore");
        context.addServlet(UserHandler.class, "/user");
        context.addServlet(RegisterUserHandler.class, "/registeruser");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{context});
        server.setHandler(handlers);

        return server;
    }

    public static void main(String[] args) throws Exception {
        int port = 8000;
        Server server = createServer(port);
        server.start();
        server.join();
    }
}
