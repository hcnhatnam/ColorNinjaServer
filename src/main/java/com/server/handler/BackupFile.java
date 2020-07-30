/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.handler;

import com.server.model.BaseModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author namhcn
 */
public class BackupFile extends BaseModel {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formmat = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        String path = "/tmp/backup/" + formmat.format(ldt) + ".zip";
        pack("/home/doleduy/dataMongo", path);
        resp.setContentType("application/zip");
        resp.setHeader("Content-disposition", "attachment; filename="+formmat.format(ldt) + ".zip");

        try (FileInputStream in =new FileInputStream(path);
                OutputStream out = resp.getOutputStream()) {
            File file = new File(path);

            byte[] buffer = new byte[(int) file.length()];
            int numBytesRead;
            while ((numBytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, numBytesRead);
            }
        }
    }

    public static void pack(String sourceDirPath, String zipFilePath) {
        try {

            Path p = Files.createFile(Paths.get(zipFilePath));
            try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
                Path pp = Paths.get(sourceDirPath);
                Files.walk(pp)
                        .filter(path -> !Files.isDirectory(path))
                        .forEach(path -> {
                            ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                            try {
                                zs.putNextEntry(zipEntry);
                                Files.copy(path, zs);
                                zs.closeEntry();
                            } catch (IOException e) {
                                System.err.println(e);
                            }
                        });
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }

    }

    public static void main(String[] args) {
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formmat = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        String path = "/tmp/backup/" + formmat.format(ldt) + ".zip";
        pack("/dataMongo", path);

    }
}
