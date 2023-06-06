package com.connectu.connectuapi.controller;
import com.connectu.connectuapi.service.IDyReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/DyReply")
public class DyReplyController {
    @Autowired
    private IDyReplyService dyReplyService;
    @PostMapping("/addFakeDyReply")
    public String addFakeDyThreads() {
        dyReplyService.addFakeDyReply(50);
        return "Fake DyReply added successfully!";
    }
}
