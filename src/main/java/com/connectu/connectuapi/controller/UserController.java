package com.connectu.connectuapi.controller;

import com.connectu.connectuapi.domain.User;
import com.connectu.connectuapi.domain.UserInfo;
import com.connectu.connectuapi.service.IUserInfoService;
import com.connectu.connectuapi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserInfoService userInfoService;

    //    @PostMapping
//    public Result save(User user, UserInfo userInfo){
//        boolean flag = userService.save(user);
//        userInfo.setUserId(user.getUserId());
//        boolean flag2 = userInfoService.save(userInfo);
//
//
//        return new Result(flag&&flag2?Code.SAVE_OK: Code.SAVE_ERR, flag&&flag2);
//    }
    @PostMapping
    public Result save(@RequestBody User user) {
        System.out.println(user);
        boolean flag = userService.save(user);
        return new Result(flag ? Code.SAVE_OK : Code.SAVE_ERR, flag, flag ?"Success!":"Failed!");
    }

    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id) {
        boolean flag2 = userInfoService.removeById(id);
        boolean flag = userService.removeById(id);
        return new Result(flag ? Code.DELETE_OK : Code.DELETE_ERR, flag, flag ?"Success!":"Failed!");
    }

    @PutMapping
    public Result updateById(@RequestBody User user) {
        boolean flag = userService.updateById(user);
        return new Result(flag ? Code.UPDATE_OK : Code.UPDATE_ERR, flag, flag ?"Success!":"Failed!");
    }

    @GetMapping("/{id}")
    public Result getUserById(@PathVariable Integer id) {
        User user = userService.getById(id);
        Integer code = user != null ? Code.GET_OK : Code.GET_ERR;
        String msg = user != null ? "success!!" : "the user do NOT exist";
        return new Result(code, user, msg);
    }

    @GetMapping("/userinfo/{id}")
    public Result getUserInfoById(@PathVariable Integer id) {
        UserInfo userInfo = userInfoService.getById(id);
        Integer code = userInfo != null ? Code.GET_OK : Code.GET_ERR;
        String msg = userInfo != null ? "success!!" : "the user's information do NOT exist";
        return new Result(code, userInfo, msg);
    }

    @GetMapping
    public Result getAllUsers() {
        List<User> users = userService.list(null);
        Integer code = users != null ? Code.GET_OK : Code.GET_ERR;
        String msg = users != null ? "success!!" : "the user do NOT exist";
        return new Result(code, users, msg);
    }

    @GetMapping("/userinfo")
    public Result getAllUserInfo() {
        List<UserInfo> userInfoList = userInfoService.list(null);
        Integer code = userInfoList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = userInfoList != null ? "success!!" : "the user's information do NOT exist";
        return new Result(code, userInfoList, msg);
    }
}
