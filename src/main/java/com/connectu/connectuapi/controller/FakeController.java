package com.connectu.connectuapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.connectu.connectuapi.dao.DyThreadHashtagDao;
import com.connectu.connectuapi.domain.Category;
import com.connectu.connectuapi.domain.Friendship;
import com.connectu.connectuapi.domain.Hashtag;
import com.connectu.connectuapi.domain.dyThreadHashtag;
import com.connectu.connectuapi.service.*;
import com.connectu.connectuapi.service.impl.StorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@Api(tags ="資料產生器")
@RequestMapping("/fake")
public class FakeController extends  BaseController {
    @Autowired
    private  IFavoriteDyThreadService favoriteDyThreadService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private  IFavoriteThreadService favoriteThreadService;
    @Autowired
    private ICategoryService iCategoryService;
    @Autowired
    private IHashtagService iHashtagService;
    @Autowired
    private DyThreadHashtagDao dyThreadHashtagDao;
    @Autowired
    private IDyThreadHashtagService iDyThreadHashtagService;
    @ApiOperation("新增dyThreadHashtag資料")
    @PostMapping("/dyThreadHashtag")
    public String createDyThreadHashtags() {
        List<dyThreadHashtag> dyThreadHashtags = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            dyThreadHashtag dyThreadHashtag = new dyThreadHashtag();
            dyThreadHashtag.setDyThreadId((int) (Math.random() * 105) + 1);
            dyThreadHashtag.setDyHashtagId((int) (Math.random() * 12) + 1);
            dyThreadHashtags.add(dyThreadHashtag);
            System.out.println("第"+i+"筆");
        }

        iDyThreadHashtagService.saveBatch(dyThreadHashtags);

        return "成功新增500筆dyThreadHashtag資料";
    }
    @ApiOperation("HashTag修改圖片")
    @PutMapping("/hashTagPicture/{threadId}")
    public String hashTagPicture(@PathVariable Integer hashTagId,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
                             HttpSession session){
        String picture= storageService.uploadToS3(files, session).toString();
        Hashtag hashtag=new Hashtag();
        hashtag.setHashtagId(hashTagId);
        if(!(files.get(0).isEmpty())) {
            String paths="";
            for (String path : storageService.uploadToS3(files, session)) {
                paths += path + "|";
                System.out.println(path);
            }
            hashtag.setPicture(paths.substring(0,paths.length()-1));

        }
        iHashtagService.updateById(hashtag);
        return picture;
    }
    @ApiOperation("HashTag修改圖片")
    @PutMapping("/CategoryPicture/{CategoryId}")
    public String CategoryPicture(@PathVariable Integer CategoryId,
                             @RequestPart(value = "files", required = false) List<MultipartFile> files,
                             HttpSession session){
        String picture= storageService.uploadToS3(files, session).toString();
        Category category=new Category();
       // Hashtag hashtag=new Hashtag();
        category.setCategoryId(CategoryId);
        if(!(files.get(0).isEmpty())) {
            String paths="";
            for (String path : storageService.uploadToS3(files, session)) {
                paths += path + "|";
                System.out.println(path);
            }
            category.setPicture(paths.substring(0,paths.length()-1));

        }
        iCategoryService.updateById(category);
        return picture;
    }
    @ApiOperation("動態收藏產生器")
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
    @ApiOperation("收藏產生器")
    @GetMapping("/insertFavoriteThread")
    public String insertFavoriteThread() {
        int count = 30;
        int userIdStart = 1;
        int userIdEnd = 98;
        int threadIdStart = 1;
        int threadIdEnd = 75;
        favoriteThreadService.insertBatchRandomData(count, userIdStart, userIdEnd, threadIdStart, threadIdEnd);
        return "Data inserted successfully";
    }
    @Autowired
    private IFriendshipService friendshipService;
    @ApiOperation("好友產生器")
    @PostMapping("/generateFriendship")
    public String generateFriendship() {
        int userIdStart = 1;
        int userIdEnd = 98;
        int friendshipCount = 300; // 要生成的Friendship數量
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