package com.connectu.connectuapi.controller;

import com.connectu.connectuapi.service.IFavoriteDyThreadService;
import com.connectu.connectuapi.service.IFavoriteThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fake")
public class FakeController {
    @Autowired
    private  IFavoriteDyThreadService favoriteDyThreadService;

    @Autowired
    private  IFavoriteThreadService favoriteThreadService;

    @GetMapping("/insertDyFavoriteThread")
    public String insertDyFavoriteThread() {
        int count = 1500;
        int userIdStart = 1;
        int userIdEnd = 98;
        int dyThreadIdStart = 1;
        int dyThreadIdEnd = 105;
        favoriteDyThreadService.insertBatchRandomData(count, userIdStart, userIdEnd, dyThreadIdStart, dyThreadIdEnd);
        return "Data inserted successfully";
    }
    @GetMapping("/insertFavoriteThread")
    public String insertFavoriteThread() {
        int count = 1500;
        int userIdStart = 1;
        int userIdEnd = 98;
        int threadIdStart = 1;
        int threadIdEnd = 75;
        favoriteThreadService.insertBatchRandomData(count, userIdStart, userIdEnd, threadIdStart, threadIdEnd);
        return "Data inserted successfully";
    }
}