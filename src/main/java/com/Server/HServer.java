/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Server;

import com.server.handler.AppConfigHandler;
import com.server.handler.BackupFile;
import com.server.handler.EventGameHandler;
import com.server.handler.LeaderBoardBestScoreHandler;
import com.server.handler.LeaderBoardHandler;
import com.server.handler.PermissionFilter;
import com.server.handler.RegisterUserHandler;
import com.server.handler.UploadServlet;
import com.server.handler.UserHandler;
import java.io.File;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 *
 * @author namhcn
 */
public class HServer {

    public static String FILE_PATH = "/tmp/sourceGame";

    public static Server createServer(int port) {
        Server server = new Server(port);

        ServletContextHandler context = new ServletContextHandler();
        context.addServlet(LeaderBoardHandler.class, "/leaderboard");
        context.addServlet(LeaderBoardBestScoreHandler.class, "/leaderboard/bestscore");
        context.addServlet(UserHandler.class, "/user");
        context.addServlet(RegisterUserHandler.class, "/registeruser");
        context.addServlet(EventGameHandler.class, "/eventgame");
        context.addServlet(AppConfigHandler.class, "/appconfig");
        context.addServlet(UploadServlet.class, "/upload");
        context.addServlet(BackupFile.class, "/backup");
        context.addFilter(PermissionFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        // resource base is relative to the WORKSPACE file
        File uploadDir = new File(FILE_PATH);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        resourceHandler.setResourceBase(FILE_PATH);

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context, new DefaultHandler()});
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
