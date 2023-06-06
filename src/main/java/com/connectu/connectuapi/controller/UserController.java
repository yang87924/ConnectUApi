package com.connectu.connectuapi.controller;
import com.connectu.connectuapi.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
