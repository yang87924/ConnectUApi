package com.connectu.connectuapi.controller;

import com.connectu.connectuapi.controller.util.Code;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.domain.User;
import com.connectu.connectuapi.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.List;

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
        return new Result(flag ? Code.SAVE_OK : Code.SAVE_ERR, flag, flag ? "用戶創建成功" : "用戶創建失敗");
    }

    //登入--------------------------------------------------------------
    @PostMapping("/login")
    @ApiOperation("登入")
    public Result login(@RequestBody User user, HttpSession session) {
        User loginUser = userService.login(user.getEmail(), user.getPassword());
        session.setAttribute("userId", loginUser.getUserId());
        session.setAttribute("userName", loginUser.getUserName());
        session.setAttribute("email", loginUser.getEmail());
        return new Result(Code.LOGIN_OK, loginUser, "登入成功");
    }


    //刪除用戶--------------------------------------------------------------
    @DeleteMapping("/{userId}")
    @ApiOperation("刪除用戶")
    public Result deleteById(@PathVariable Integer userId) {
        boolean flag = userService.removeById(userId);
        return new Result(flag ? Code.DELETE_OK : Code.DELETE_ERR, flag, flag ? "用戶資料刪除成功" : "用戶資料刪除失敗");
    }


    //修改用戶--------------------------------------------------------------
    @PutMapping
    @ApiOperation("修改用戶")
    public Result updateById(User user, @RequestParam("avatarFile") MultipartFile file, HttpSession session) {
        if(!file.isEmpty()) {
            user.setAvatar(upload(file, session));
        }

        boolean flag = userService.updateById(user);
        return new Result(flag ? Code.UPDATE_OK : Code.UPDATE_ERR, flag, flag ? "用戶資料更新成功" : "用戶資料更新失敗");
    }


    //查詢用戶--------------------------------------------------------------
    @GetMapping("/{userId}")
    @ApiOperation("查詢用戶")
    public Result getUserById(@PathVariable Integer userId) {
        User user = userService.getById(userId);
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



}
