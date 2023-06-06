package com.connectu.connectuapi.controller;
import com.connectu.connectuapi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.connectu.connectuapi.domain.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserService userService;
    @PostMapping("/addFakeUser")
    public String addFakeUsers() {
        userService.addFakeUsers(100);
        return "Fake users added successfully!";
    }
    @PostMapping
    public Result save(User user){
        boolean flag = userService.save(user);
        return new Result(flag?Code.SAVE_OK: Code.SAVE_ERR, flag);
    }

    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Integer id){
        boolean flag = userService.removeById(id);
        return new Result(flag?Code.DELETE_OK: Code.DELETE_ERR, flag);
    }
    @PutMapping
    public Result updateById(User user){
        boolean flag = userService.updateById(user);
        return new Result(flag?Code.UPDATE_OK: Code.UPDATE_ERR, flag);
    }
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        User user =  userService.getById(id);
        Integer code = user!=null?Code.GET_OK: Code.GET_ERR;
        String msg = user!=null?"success!!": "the user do NOT exist";
        return new Result(code, user, msg);
    }
    @GetMapping
    public Result getAll(){
        List<User> users =  userService.list(null);
        Integer code = users!=null?Code.GET_OK: Code.GET_ERR;
        String msg = users!=null?"success!!": "the user do NOT exist";
        return new Result(code, users, msg);
    }
}
