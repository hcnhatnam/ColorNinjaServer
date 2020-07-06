/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.model;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author namhcn
 */
public class BaseModel extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(BaseModel.class);

    public void returnJSon(HttpServletResponse resp, String content) {
//        resp.addHeader("Access-Control-Allow-Origin", "http://localhost:8080");
//        resp.addHeader("Access-Control-Allow-Credentials", "true");
//        resp.addHeader("Access-Control-Allow-Methods", "POST, GET,PUT, DELETE, HEAD, OPTIONS");
//        resp.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers,x-token,X-Custom-Header");

        PrintWriter out = null;
        try {
            resp.setContentType("application/json;charset=UTF-8");
            resp.setCharacterEncoding("UTF-8");
            out = resp.getWriter();
            out.println(content);
            resp.setHeader("Connection", "close");
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException ex) {
            LOGGER.error(null, ex);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public void returnJSon(HttpServletRequest req, HttpServletResponse resp, String content) {
        LOGGER.info(req.getRequestURI() + "_" + content);
//        resp.addHeader("Access-Control-Allow-Origin", "http://localhost:8080");
//        resp.addHeader("Access-Control-Allow-Credentials", "true");
//        resp.addHeader("Access-Control-Allow-Methods", "POST, GET,PUT, DELETE, HEAD, OPTIONS");
//        resp.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers,x-token,X-Custom-Header");

        PrintWriter out = null;
        try {
            resp.setContentType("application/json;charset=UTF-8");
            resp.setCharacterEncoding("UTF-8");
            out = resp.getWriter();
            out.println(content);
            resp.setHeader("Connection", "close");
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException ex) {
            LOGGER.error(null, ex);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
