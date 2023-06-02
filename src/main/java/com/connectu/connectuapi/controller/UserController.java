package com.connectu.connectuapi.controller;
import com.connectu.connectuapi.dao.impl.User;
import com.connectu.connectuapi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private IUserService userService;
    @GetMapping("/{id}")
    public User getById(@PathVariable Integer id){
        return userService.getById(id);
    }
    @GetMapping
    public List<User> getAll(){
        return userService.list(null);
    }
}
