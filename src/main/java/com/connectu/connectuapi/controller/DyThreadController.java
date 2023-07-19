package com.connectu.connectuapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.connectu.connectuapi.controller.util.Code;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.domain.*;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.exception.ThreadColumnIsNullException;
import com.connectu.connectuapi.exception.UserNotLoginException;
import com.connectu.connectuapi.service.*;
import com.connectu.connectuapi.service.impl.StorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Api(tags ="動態文章")
@RestController
@RequestMapping("/dyThreads")

public class DyThreadController extends BaseController{
    @Autowired
    private IDyThreadService dyThreadService;
    @Autowired
    private IUserDyThreadLoveService userDyThreadLoveService;
    @Autowired
    private IFavoriteDyThreadService favoriteDyThreadService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private IDyHashtagService dyHashtagService;
    @ApiOperation("熱門標籤OK")
    @GetMapping("/dyHotHashtag")
    public Result getTopThreeHashtags() {
        List<DyHashtag> dyHashtags = dyHashtagService.getTopDyThreeHashtags();
        Integer code = dyHashtags != null ? Code.GET_OK : Code.GET_ERR;
        String msg = dyHashtags != null ? "查詢熱門標籤成功" : "查無熱門標籤資料";
        return new Result(code, dyHashtags, msg);
    }
    //切換使用者按讚--------------------------------------------------------------
    //熱門文章--------------------------------------------------------------
    @GetMapping("/hotThread")
    @ApiOperation("熱門文章OK")
    public Result hotUser(HttpSession session) {
        List<DyThread> dyThread = dyThreadService.hotDyhread();
        Integer code = dyThread != null ? Code.GET_OK : Code.GET_ERR;
        String msg = dyThread != null ? "查詢熱門文章資料成功" : "查無資料";
        return new Result(code, dyThread, msg);
    }
    @PutMapping("/toggleUserLove/{DyThreadId}")
    @ApiOperation("使用者按讚+紀錄OK")
    public Result toggleUserLove(@PathVariable Integer DyThreadId, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            throw new UserNotLoginException();
        }
        Integer userId = (Integer) session.getAttribute("userId");
        int userLoveStatus = userDyThreadLoveService.toggleLove(userId, DyThreadId);
        // 取得當前文章的按讚數
        DyThread dyThread = dyThreadService.getById(DyThreadId);
        int loveCount = dyThread.getLove();
        // 根據按讚狀態回傳不同的訊息
        String message = userDyThreadLoveService.getLoveMessage(userLoveStatus);
        return new Result(Code.UPDATE_OK, dyThread , message);
    }
    //分頁查詢--------------------------------------------------------------

    @GetMapping("/pageDyThread")
    @ApiOperation("分頁查詢所有論壇文章OK")
    public Result getAllThreadPage(@RequestParam(defaultValue = "1") Integer pageNum) {
        Page<DyThread> page = new Page<>(pageNum, 4);
        QueryWrapper<DyThread> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("dyThreadId"); // 將資料庫中的資料進行反向排序
        IPage<DyThread> threadPage = dyThreadService.listWithDyPagination(page, wrapper);
        List<DyThread> threadList = threadPage.getRecords();
        Integer code = threadList != null ? Code.GET_OK : Code.GET_ERR;
        String msg = threadList != null ? "所有論壇文章資料成功" : "查無論壇文章資料";
        return new Result(code, threadList, msg);
    }
    //新增收藏文章--------------------------------------------------------------
    @PostMapping("/favorite")
    @ApiOperation(value = "新增收藏文章OK", notes = "新增使用者收藏的文章")
    public Result addFavoriteThread(@ApiParam(value = "文章 ID", required = true) @RequestParam Integer dyThreadId,
                                    HttpSession session) {
        if (session.getAttribute("userId") == null) {
            throw new UserNotLoginException();
        }
        Integer userId = getUserIdFromSession(session);
        boolean flag = dyThreadService.addFavoriteDyThread(userId, dyThreadId);
        return new Result(flag ? Code.SAVE_OK : Code.SAVE_ERR, flag, flag ? "收藏文章成功" : "收藏文章失敗");
    }
    @PostMapping
    @ApiOperation(value = "新增論壇文章", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result save(  DyThread dyThread,
                         @ApiParam(value = "文章內容", required = true) @RequestParam String content,
                         @ApiParam(value = "檔案", required = false)
                             @RequestPart(value = "file1", required = false) List<MultipartFile> file1,
                         @ApiParam(value = "檔案", required = false)
                             @RequestPart(value = "file2", required = false) List<MultipartFile> file2,
                         @ApiParam(value = "檔案", required = false)
                             @RequestPart(value = "file3", required = false) List<MultipartFile> file3,
                         @ApiParam(value = "檔案", required = false)
                             @RequestPart(value = "file4", required = false) List<MultipartFile> file4,
                        // @ApiParam(value = "Hashtags", required = false) @RequestParam(required = false) List<String> hashtags,
                         HttpSession session) {

        if (session.getAttribute("userId") == null) {
            throw new UserNotLoginException();
        }
        if(content==null||content.isEmpty()) {
            throw new ThreadColumnIsNullException();
        }
        List<MultipartFile> files = new ArrayList<>();
        //System.out.println(file1);
        if (file1 != null && !file1.isEmpty()) {
            files.addAll(file1);
        }
        if (file2 != null && !file2.isEmpty()) {
            files.addAll(file2);
        }
        if (file3 != null && !file3.isEmpty()) {
            files.addAll(file3);
        }
        if (file4 != null && !file4.isEmpty()) {
            files.addAll(file4);
        }
        String paths;
        dyThread.setUserId(getUserIdFromSession(session));
        if (!files.isEmpty() && !(files.get(0).isEmpty())) {
             paths="";
            for (String path : storageService.uploadToS3(files, session)) {
                paths += path + "▲";

            }
            System.out.println(paths);
            dyThread.setPicture(paths.substring(0,paths.length()-1));

        }else {
            dyThread.setPicture(""); // 设置为空字符串或适当的空值
        }
        boolean flag = dyThreadService.save(dyThread);
//        if (flag && hashtags != null && !hashtags.isEmpty()) {
           //threadService.handleHashtags(thread, hashtags);
//        }

        return new Result(flag ? Code.SAVE_OK : Code.SAVE_ERR, flag, flag ? "論壇文章新增成功" : "論壇文章新增失敗");
    }

    //修改文章
    @PutMapping("{dyThreadid}")
    @ApiOperation("修改動態文章")
    public Result updateById( DyThread dyThread, @PathVariable Integer dyThreadid,
                              @ApiParam(value = "文章內容", required = true) @RequestParam String content,
                              @ApiParam(value = "檔案", required = false)
                                  @RequestPart(value = "files", required = false) List<MultipartFile> files,
                              HttpSession session
                              )
    {
        String paths;
        //dyThread.setUserId(getUserIdFromSession(session));
        if (!files.isEmpty() && !(files.get(0).isEmpty())) {
            paths="";
            for (String path : storageService.uploadToS3(files, session)) {
                paths += path + "▲";

            }
            System.out.println(paths);
            dyThread.setPicture(paths.substring(0,paths.length()-1));

        }else {
            dyThread.setPicture(""); // 设置为空字符串或适当的空值
        }
        dyThread.setDyThreadId(dyThreadid);
        dyThread.setContent(content);

        boolean flag = dyThreadService.updateById(dyThread);
        return new Result(flag ? Code.UPDATE_OK : Code.UPDATE_ERR, flag, flag ?"動態文章更新成功":"動態文章更新失敗");
    }
    //移除收藏文章
    //移除收藏文章
    @DeleteMapping("/favorite/{favoriteDyThreadId}")
    @ApiOperation(value = "移除收藏文章OK", notes = "移除使用者收藏的文章")
    public Result removeFavoriteThread(@ApiParam(value = "文章 ID", required = true) @PathVariable Integer favoriteDyThreadId ,
                                       HttpSession session) {
        if (session.getAttribute("userId") == null) {
            throw new UserNotLoginException();
        }
        Integer userId = getUserIdFromSession(session);
        boolean flag = favoriteDyThreadService.removeById(favoriteDyThreadId);
        return new Result(flag ? Code.DELETE_OK : Code.DELETE_ERR, flag, flag ? "移除收藏文章成功" : "移除收藏文章失敗");
    }

    //取得使用者的所有文章
    @GetMapping("/userDyThread/{userId}")
    @ApiOperation("取得使用者的所有動態文章OK")
    public Result getUserThread(@PathVariable Integer userId,HttpSession session) {
        if(userId==0){
            userId = getUserIdFromSession(session);
        }
        List<DyThread> dythread = dyThreadService.getUserDyThreadById(userId);
        Integer code = dythread != null ? Code.GET_OK : Code.GET_ERR;
        String msg = dythread != null ? "查詢使用者動態文章資料成功" : "查無動態文章資料";
        return new Result(code, dythread, msg);
    }

    //刪除文章
    @DeleteMapping("/{DyThreadid}")
    @ApiImplicitParam(name = "DyThreadid", value = "動態文章id")
    @ApiOperation("刪除動態文章")
    public Result deleteById(@PathVariable Integer DyThreadid) {
        boolean flag = dyThreadService.removeById(DyThreadid);
        return new Result(flag ? Code.DELETE_OK : Code.DELETE_ERR, flag, flag ?"動態文章刪除成功":"動態文章刪除失敗");
    }
    //假資料
    @ApiIgnore    // 忽略这个api
    @PostMapping("/addFakeDyThread")
    public String addFakeDyThreads() {
        dyThreadService.addFakeDyThread(50);
        return "Fake DyThreads added successfully!";
    }
    //關鍵字搜尋--------------------------------------------------------------
    @GetMapping("/search")
    @ApiOperation("關鍵字搜尋")
    public Result searchThreadsByKeyword(@ApiParam("關鍵字") @RequestParam String keyword) {
        return dyThreadService.searchDyThreads(keyword);
    }
}
