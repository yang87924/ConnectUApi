package com.connectu.connectuapi.controller;

import com.connectu.connectuapi.controller.util.Code;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.domain.User;
import com.connectu.connectuapi.exception.file.*;
import com.connectu.connectuapi.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Api(tags = "使用者")
@RestController
@RequestMapping("/users")
public class UserController extends BaseController {
    @Autowired
    private IUserService userService;
    //假資料

    @PostMapping("/addFakeUser")
    @ApiIgnore    // 忽略这个api
    public String addFakeUsers() {
        userService.addFakeUsers(100);
        return "Fake users added successfully!";
    }


//創建用戶--------------------------------------------------------------
    @PostMapping
    @ApiOperation("創建用戶")
    public Result save(@RequestBody User user) {
        boolean flag = userService.save(user);
        return new Result(flag ? Code.SAVE_OK : Code.SAVE_ERR, flag, flag ?"用戶創建成功":"用戶創建失敗");
    }
//登入--------------------------------------------------------------
    @PostMapping ("/login")
    @ApiOperation("登入")
    public Result getAllUserInfo(@RequestBody User user, HttpSession session) {
        User loginUser = userService.login(user.getEmail(), user.getPassword());
        session.setAttribute("userId", loginUser.getUserId());
        session.setAttribute("userName", loginUser.getUserName());
        session.setAttribute("email", loginUser.getEmail());
        return new Result(Code.LOGIN_OK, loginUser, "登入成功");
    }




//刪除用戶--------------------------------------------------------------
    @DeleteMapping("/{id}")
    @ApiOperation("刪除用戶")
    public Result deleteById(@PathVariable Integer id) {
        boolean flag = userService.removeById(id);
        return new Result(flag ? Code.DELETE_OK : Code.DELETE_ERR, flag, flag ?"用戶資料刪除成功":"用戶資料刪除失敗");
    }





//修改用戶--------------------------------------------------------------
    @PutMapping
    @ApiOperation("修改用戶")
    public Result updateById(@RequestBody User user) {
        boolean flag = userService.updateById(user);
        return new Result(flag ? Code.UPDATE_OK : Code.UPDATE_ERR, flag, flag ?"用戶資料更新成功":"用戶資料更新失敗");
    }






//查詢用戶--------------------------------------------------------------
    @GetMapping("/{id}")
    @ApiOperation("查詢用戶")
    public Result getUserById(@PathVariable Integer id) {
        User user = userService.getById(id);
        Integer code = user != null ? Code.GET_OK : Code.GET_ERR;
        String msg = user != null ? "用戶資料取得成功" : "查無此用戶";
        return new Result(code, user, msg);
    }


    @GetMapping
    @ApiOperation("查詢所有用戶")
    public Result getAllUsers() {
        List<User> users = userService.list(null);
        Integer code = users != null ? Code.GET_OK : Code.GET_ERR;
        String msg = users != null ? "所有用戶資料取得成功" : "查無用戶資料";
        return new Result(code, users, msg);
    }

//更新用戶頭像--------------------------------------------------------------



    /** 头像文件大小的上限值(10MB) */
    public static final int AVATAR_MAX_SIZE = 10 * 1024 * 1024;
    /** 允许上传的头像的文件类型 */
    public static final List<String> AVATAR_TYPES = new ArrayList<>();

    /** 初始化允许上传的头像的文件类型 */
    static {
        AVATAR_TYPES.add("image/jpeg");
        AVATAR_TYPES.add("image/png");
        AVATAR_TYPES.add("image/bmp");
        AVATAR_TYPES.add("image/gif");
    }

    @PostMapping("change_avatar")
    public Result changeAvatar(@RequestParam("file") MultipartFile file, HttpSession session) {
        // 判断上传的文件是否为空
        if (file.isEmpty()) {
            // 是：抛出异常
            throw new FileEmptyException("上傳的頭像文件不允許為空");
        }

        // 判断上传的文件大小是否超出限制值
        if (file.getSize() > AVATAR_MAX_SIZE) { // getSize()：返回文件的大小，以字节为单位
            // 是：抛出异常
            throw new FileSizeException("不允許上傳超過" + (AVATAR_MAX_SIZE / 1024) + "KB的頭像文件");
        }

        // 判断上传的文件类型是否超出限制
        String contentType = file.getContentType();
        if (!AVATAR_TYPES.contains(contentType)) {
            throw new FileTypeException("不支持使用該類型的文件作為頭像，允許的文件類型：" + AVATAR_TYPES);
        }


        String parent = session.getServletContext().getRealPath("upload");
        System.out.println(parent);

        File dir = new File(parent);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String suffix = "";
        String originalFilename = file.getOriginalFilename();
        int beginIndex = originalFilename.lastIndexOf(".");
        if (beginIndex > 0) {
            suffix = originalFilename.substring(beginIndex);
        }
        String filename = UUID.randomUUID().toString() + suffix;

        File dest = new File(dir, filename);
        try {
            file.transferTo(dest);
        } catch (IllegalStateException e) {
            throw new FileStateException("文件狀態異常，可能文件已被移動或刪除");
        } catch (IOException e) {
            throw new FileUploadIOException("上傳文件時讀寫錯誤，請稍後重新嘗試");
        }

        // 头像路径
        String avatar = "/upload/" + filename;

        User loginUser = userService.getById(getUserIdFromSession(session));
        loginUser.setAvatar(avatar);
        // 将头像写入到数据库中
        userService.save(loginUser);

        // 返回成功头像路径
        return new Result(Code.SAVE_OK, avatar, "大頭貼存取成功");
    }



}
