package com.connectu.connectuapi.controller;

import com.connectu.connectuapi.service.IDyThreadService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags ="動態文章")
@RestController
@RequestMapping("/DyThreads")
public class DyThreadController {
    @Autowired
    private IDyThreadService dyThreadService;
    //假資料
    @ApiIgnore    // 忽略这个api
    @PostMapping("/addFakeDyThread")
    public String addFakeDyThreads() {
        dyThreadService.addFakeDyThread(50);
        return "Fake DyThreads added successfully!";
    }
}
