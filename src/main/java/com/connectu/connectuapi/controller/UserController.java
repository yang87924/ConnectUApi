package com.connectu.connectuapi.controller;

import com.connectu.connectuapi.controller.util.Code;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.domain.User;
import com.connectu.connectuapi.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
@Api(tags = "使用者")
@RestController
@RequestMapping("/users")
public class UserController {
    //假資料
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
    public Result getAllUserInfo(@RequestBody User user) {
        User flag = userService.login(user.getEmail(), user.getPassword());
        return new Result(Code.LOGIN_OK, flag, "登入成功");
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



}
