package com.connectu.connectuapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.connectu.connectuapi.domain.Friendship;
import com.connectu.connectuapi.service.IFavoriteDyThreadService;
import com.connectu.connectuapi.service.IFavoriteThreadService;
import com.connectu.connectuapi.service.IFriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
    @Autowired
    private IFriendshipService friendshipService;
    @PostMapping("/generateFriendship")
    public String generateFriendship() {
        int userIdStart = 1;
        int userIdEnd = 98;
        int friendshipCount = 2000; // 要生成的Friendship數量
        Set<String> existingFriendships = new HashSet<>(); // 用於檢查是否已存在相同的資料
        List<Friendship> newFriendships = new ArrayList<>(); // 用於批量新增Friendship
        Random random = new Random();
        while (newFriendships.size() < friendshipCount) {
            int followerId = getRandomNumber(userIdStart, userIdEnd);
            int followingId = getRandomNumber(userIdStart, userIdEnd);
            String friendshipKey = followerId + "-" + followingId;
            if (existingFriendships.contains(friendshipKey)) {
                continue; // 如果已存在相同的資料，則跳過此次循環，不新增Friendship
            }
            existingFriendships.add(friendshipKey);
            Friendship friendship = new Friendship();
            friendship.setFollowerId(followerId);
            friendship.setFollowingId(followingId);
            newFriendships.add(friendship);
        }
        friendshipService.saveBatch(newFriendships); // 批量新增Friendship
        return "Friendship generated successfully!";
    }
    // 生成指定範圍內的亂數
    private int getRandomNumber(int start, int end) {
        Random random = new Random();
        return random.nextInt(end - start + 1) + start;
    }
}