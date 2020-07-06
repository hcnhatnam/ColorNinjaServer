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
import com.Server.HServer;
import com.colorninja.entity.EventGame;
import com.database.EventGameDB;
import com.server.entity.HReqParam;
import com.server.entity.ResultObject;
import com.server.model.BaseModel;
import java.io.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

public class UploadServlet extends BaseModel {

    private static final Logger LOGGER = Logger.getLogger(UploadServlet.class);

    private final int MAX_FILE_SIZE = 512 * 1024;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse resp) {
        ResultObject resultObject = new ResultObject();

        returnJSon(resp, resultObject.toString());
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
//
//            String eventname = HReqParam.getString(req, "eventname");
//            String description = HReqParam.getString(req, "description");

            if (ServletFileUpload.isMultipartContent(req)) {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                upload.setFileSizeMax(MAX_FILE_SIZE);

                File uploadDir = new File(HServer.FILE_PATH);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                List<FileItem> formItems = upload.parseRequest(req);
                EventGame eventGame = new EventGame();
                if (formItems != null && formItems.size() > 0) {
                    for (FileItem item : formItems) {
                        String fieldname = item.getFieldName();
                        switch (fieldname) {
                            case "file":
                                String fileName = new File(item.getName()).getName();
                                String subUrl = System.currentTimeMillis() + File.separator + fileName;
                                String filePathAbs = HServer.FILE_PATH + File.separator + subUrl;
                                File storeFile = new File(filePathAbs);
                                item.write(storeFile);
                                eventGame.setBackgroundUrl("localhost:8000/" + subUrl);
                                break;
                            case "eventname":
                                String eventname = item.getString("UTF-8");
                                eventGame.setEventname(eventname);
                                break;
                            case "description":
                                String description = item.getString("UTF-8");
                                eventGame.setDescription(description);
                                break;
                        }

                    }
                }
                EventGameDB.INSTANCE.insert(eventGame);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
