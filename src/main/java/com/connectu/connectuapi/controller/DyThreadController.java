package com.connectu.connectuapi.controller;
import com.connectu.connectuapi.service.IDyThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/DyThreads")
public class DyThreadController {
    @Autowired
    private IDyThreadService dyThreadService;

}
