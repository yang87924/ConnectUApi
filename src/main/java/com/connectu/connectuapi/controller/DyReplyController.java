package com.connectu.connectuapi.controller;

import com.connectu.connectuapi.service.IDyReplyService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags ="動態回復")
@RestController
@RequestMapping("/DyReply")
public class DyReplyController {
    @Autowired
    private IDyReplyService dyReplyService;
    //假資料
    @ApiIgnore    // 忽略这个api
    @PostMapping("/addFakeDyReply")
    public String addFakeDyThreads() {
        dyReplyService.addFakeDyReply(50);
        return "Fake DyReply added successfully!";
    }
}
