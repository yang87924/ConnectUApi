package com.connectu.connectuapi.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.connectu.connectuapi.exception.file.*;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StorageService {
    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3client;
    public static final int AVATAR_MAX_SIZE = 10 * 1024 * 1024;

    public static final List<String> AVATAR_TYPES = new ArrayList<>();

    static {
        AVATAR_TYPES.add("image/jpeg");
        AVATAR_TYPES.add("image/png");
        AVATAR_TYPES.add("image/bmp");
        AVATAR_TYPES.add("image/gif");
    }

    public List<String> uploadToS3(List<MultipartFile> files, HttpSession session) {

        if (files == null || files.isEmpty()) {
            throw new FileEmptyException();
        }

        List<String> pathList = new ArrayList<>();

        for (MultipartFile file : files) {


            if (file.isEmpty()) {
                throw new FileEmptyException();
            }
            if (file.getSize() > AVATAR_MAX_SIZE) {
                throw new FileSizeException();
            }
            String contentType = file.getContentType();
            if (!AVATAR_TYPES.contains(contentType)) {
                throw new FileTypeException();
            }


            String suffix = "";
            String originalFilename = file.getOriginalFilename();
            int beginIndex = originalFilename.lastIndexOf(".");
            if (beginIndex > 0) {
                suffix = originalFilename.substring(beginIndex);
            }
            String filename = UUID.randomUUID().toString() + suffix;
            try {
                File fileObj = convertMultipartFileToFile(file);
                s3client.putObject(new PutObjectRequest(bucketName, filename, fileObj));
                fileObj.delete();
            } catch (IllegalStateException e) {
                throw new FileStateException();
            }

            pathList.add("https://seeiebucket.s3.ap-northeast-1.amazonaws.com/" + filename);

        }
        return pathList;
    }


    private File convertMultipartFileToFile(MultipartFile file){
        File convertedFile = new File(file.getOriginalFilename());
        try(FileOutputStream fos = new FileOutputStream(convertedFile)){
            fos.write(file.getBytes());
        }catch (IOException e){
            throw new FileUploadIOException();
        }
        return convertedFile;
    }
}
