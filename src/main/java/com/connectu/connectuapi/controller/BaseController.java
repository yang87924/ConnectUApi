package com.connectu.connectuapi.controller;

import com.connectu.connectuapi.config.WebsocketConfig;
import com.connectu.connectuapi.exception.file.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;


public class BaseController {

    public final Integer getUserIdFromSession(HttpSession session) {
        return Integer.valueOf(session.getAttribute("userId").toString());
    }


    protected final String getUserNameFromSession(HttpSession session) {
        return session.getAttribute("userName").toString();
    }

    protected final String getEmailFromSession(HttpSession session) {
        return session.getAttribute("email").toString();
    }

    protected final String getPicturePathFromSession(HttpSession session) {
        return session.getAttribute("picPath").toString();
    }



//
//    public static final int AVATAR_MAX_SIZE = 10 * 1024 * 1024;
//
//    public static final List<String> AVATAR_TYPES = new ArrayList<>();
//
//    static {
//        AVATAR_TYPES.add("image/jpeg");
//        AVATAR_TYPES.add("image/png");
//        AVATAR_TYPES.add("image/bmp");
//        AVATAR_TYPES.add("image/gif");
//    }
//

//
//    public List<String> upload(List<MultipartFile> files, HttpSession session) {
//        if (files == null || files.isEmpty()) {
//            throw new FileEmptyException();
//        }
//
//        List<String> pathList = new ArrayList<>();
//
//        for (MultipartFile file : files) {
//
//
//            if (file.isEmpty()) {
//                throw new FileEmptyException();
//            }
//            if (file.getSize() > AVATAR_MAX_SIZE) {
//                throw new FileSizeException();
//            }
//            String contentType = file.getContentType();
//            if (!AVATAR_TYPES.contains(contentType)) {
//                throw new FileTypeException();
//            }
//
//            String parent = session.getServletContext().getRealPath("upload");
//
//            File dir = new File(parent);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//            String suffix = "";
//            String originalFilename = file.getOriginalFilename();
//            int beginIndex = originalFilename.lastIndexOf(".");
//            if (beginIndex > 0) {
//                suffix = originalFilename.substring(beginIndex);
//            }
//            String filename = UUID.randomUUID().toString() + suffix;
//
//            File dest = new File(dir, filename);
//            try {
//                file.transferTo(dest);
//            } catch (IllegalStateException e) {
//                throw new FileStateException();
//            } catch (IOException e) {
//                throw new FileUploadIOException();
//            }
//
//            String avatar = parent + "\\" + filename;
//            String revisedPath = avatar.replace("\\", "/");
//            pathList.add(revisedPath);
//
//        }
//        return pathList;
//    }




}
