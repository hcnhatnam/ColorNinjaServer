/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.handler;

import com.server.entity.ResultObject;
import com.server.model.BaseModel;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author namhcn
 */
public class PermissionFilter extends BaseModel implements Filter {

    private static final Logger LOGGER = Logger.getLogger(EventGameHandler.class);

    @Override
    public void init(FilterConfig fc) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        try {

            resp.addHeader("Access-Control-Allow-Origin", "http://colorninjagame.tk");
//            resp.addHeader("Access-Control-Allow-Origin", "http://localhost:8080");
            resp.addHeader("Access-Control-Allow-Credentials", "true");
            resp.addHeader("Access-Control-Allow-Methods", "POST, GET,PUT, DELETE, HEAD, OPTIONS");
            resp.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers,x-token,X-Custom-Header");
            chain.doFilter(servletRequest, resp);
        } catch (IOException | ServletException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        returnJSon(resp, "");
    }

}
