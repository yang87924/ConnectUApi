package com.connectu.connectuapi.controller;
import com.connectu.connectuapi.service.IReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/Replys")
public class ReplyController {
    @Autowired
    private IReplyService replyService;

}
