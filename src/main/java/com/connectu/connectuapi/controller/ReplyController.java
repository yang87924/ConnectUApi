package com.connectu.connectuapi.controller;

import com.connectu.connectuapi.service.IReplyService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags ="論壇回復")
@RestController
@RequestMapping("/Replys")
public class ReplyController {
    @Autowired
    private IReplyService replyService;
    //假資料
    @ApiIgnore    // 忽略这个api
    @PostMapping("/addFakeReply")
    public String addFakeReply() {
        replyService.addFakeReply(50);
        return "Fake Reply added successfully!";
    }

}
