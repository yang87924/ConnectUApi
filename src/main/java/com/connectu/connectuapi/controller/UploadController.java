package com.connectu.connectuapi.controller;

import com.connectu.connectuapi.controller.util.Code;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.domain.User;
import com.connectu.connectuapi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
@RequestMapping("/upload")
@RestController
public class UploadController extends BaseController{

    @Autowired
    IUserService userService;

    @PostMapping
    public Result upload(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
        // 获取上传文件的原始文件名
        String originalFilename = file.getOriginalFilename();
        // 获取上下文的绝对路径
        String realPath = request.getServletContext().getRealPath("upload");
        System.out.println(realPath);
        // 创建File文件对象
        File dir = new File(realPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        // 自定义上传文件名
        String fileName = UUID.randomUUID().toString();
        // 获取上传文件扩展名
        String suffix = "";
        int beginIndex = originalFilename.lastIndexOf(".");
        if (beginIndex > 0) {
            suffix = originalFilename.substring(beginIndex);
        }
        String fullFilename = fileName + suffix;
        // 调用MultipartFile参数对象的transferTo()方法即可保存上传的文件
        file.transferTo(new File(dir, fullFilename));
//        System.out.println(realPath+"\\"+fullFilename);

        User user = userService.getById(getUserIdFromSession(request.getSession()));
        user.setAvatar(realPath+"\\"+fullFilename);
        if(userService.updateById(user)) {
            return new Result(Code.UPLOAD_OK, realPath + "\\" + fullFilename, "上傳成功");
        }
        return new Result(Code.UPDATE_ERR, "更新資料失敗");
    }

}
