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
import com.connectu.connectuapi.exception.file.*;
import com.connectu.connectuapi.service.*;
import com.connectu.connectuapi.service.impl.StorageService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.connectu.connectuapi.service.utils.faker.getSystemTime;

@Api(tags ="論壇")
@RestController
@RequestMapping("/threads")


public class ThreadController extends BaseController{
    @Autowired
    private IThreadService threadService;
    @Autowired
    private IUserThreadLoveService userThreadLoveService;
    @Autowired
    private IFavoriteThreadService favoriteThreadService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private IHashtagService hashtagService;
    //查詢使用者的所有文章--------------------------------------------------------------
    @GetMapping("/userThread/{userId}")
    @ApiOperation("查詢使用者的所有論壇文章")
    public Result getUserThread(@PathVariable Integer userId, HttpSession session) {
        if (userId == 0) {
            userId = getUserIdFromSession(session);
        }
        List<Thread> threads = threadService.getUserThread(userId);
        //System.out.println(threadService.getUserThread(userId)); // 打印结果

        Integer code = threads != null ? Code.GET_OK : Code.GET_ERR;
        String msg = threads != null ? "查詢熱門文章資料成功" : "查無資料";
        return new Result(code, threads, msg);
    }


    //刪除文章--------------------------------------------------------------
    @DeleteMapping("/deleteByThreadId")
    @ApiOperation("刪除指定threadId的論壇文章")
    public Result deleteByThreadId() {
        boolean flag = threadService.deleteByThreadId(3);
        return new Result(flag ? Code.DELETE_OK : Code.DELETE_ERR, flag, flag ?"指定論壇文章刪除成功":"指定論壇文章刪除失敗");
    }

    //查詢所有文章--------------------------------------------------------------
    // 分页查询所有文章，固定每页返回4条数据
    @GetMapping
    @ApiOperation("分页查询所有論壇文章")
    public Result getThreadByPage(@RequestParam(defaultValue = "1") Integer pageNum) {
        Page<Thread> page = new Page<>(pageNum, 4);
        page = threadService.getThreadByPage(page);
        Integer code = page != null ? Code.GET_OK : Code.GET_ERR;
        String msg = page != null ? "分頁查詢論壇文章成功" : "分頁查詢論壇文章失敗";
        return new Result(code, page, msg);
    }

    //分頁查詢--------------------------------------------------------------
    @GetMapping("/pageThread")
    @ApiOperation("分頁查詢所有論壇文章OK")
    public Result getAllThreadPage(@RequestParam(defaultValue = "1") Integer pageNum) {
        Page<Thread> page = new Page<>(pageNum, 4);
        Page<Thread> threadPage = threadService.getThreadByPage(page);
        Integer code = threadPage != null ? Code.GET_OK : Code.GET_ERR;
        String msg = threadPage != null ? "分頁查詢論壇文章成功" : "分頁查詢論壇文章失敗";
        return new Result(code, threadPage.getRecords(), msg);
    }
    @ApiOperation(value = "熱門標籤OK")
    @GetMapping("/HotHashtag")
    public Result getTopThreeHashtags() {
        List<Hashtag> hashtags = hashtagService.getTopThreeHashtags();
        Integer code = hashtags != null ? Code.GET_OK : Code.GET_ERR;
        String msg = hashtags != null ? "查詢熱門標籤成功" : "查無熱門標籤資料";
        return new Result(code, hashtags, msg);
    }
    @ApiOperation("所有標籤")
    @GetMapping("/AllHashtag")
    public Result AllThreeHashtags() {
        List<Hashtag> dyHashtags = hashtagService.list();
        Integer code = dyHashtags != null ? Code.GET_OK : Code.GET_ERR;
        String msg = dyHashtags != null ? "查詢所有標籤成功" : "查無標籤資料";
        return new Result(code, dyHashtags, msg);
    }
    //假資料--------------------------------------------------------------
    @ApiIgnore    // 忽略这个api
    @PostMapping("/addFakeThread")
    public String addFakeThread() {
        threadService.addFakeThread(50);
        return "Fake Thread added successfully!";
    }
    //新增收藏文章--------------------------------------------------------------
    @PostMapping("/favorite")
    @ApiOperation(value = "新增收藏文章OK", notes = "新增使用者收藏的文章")
    public Result addFavoriteThread(@ApiParam(value = "文章 ID", required = true) @RequestParam Integer threadId,
                                    HttpSession session) {
        if (session.getAttribute("userId") == null) {
            throw new UserNotLoginException();
        }
        Integer userId = getUserIdFromSession(session);
        boolean flag = threadService.addFavoriteThread(userId, threadId);
        return new Result(flag ? Code.SAVE_OK : Code.SAVE_ERR, flag, flag ? "收藏文章成功" : "收藏文章失敗");
    }

    @PostMapping
    @ApiOperation(value = "新增論壇文章", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result save(Thread thread,
                       @ApiParam(value = "文章標題", required = true) @RequestParam String title,
                       @ApiParam(value = "文章內容", required = true) @RequestParam String content,
                       @ApiParam(value = "文章分類 ID", required = true) @RequestParam Integer categoryId,
                       @ApiParam(value = "檔案", required = false)
                       @RequestPart(value = "file0", required = false) List<MultipartFile> file1,
                       @ApiParam(value = "檔案", required = false)
                           @RequestPart(value = "file1", required = false) List<MultipartFile> file2,
                       @ApiParam(value = "檔案", required = false)
                           @RequestPart(value = "file2", required = false) List<MultipartFile> file3,
                       @ApiParam(value = "檔案", required = false)
                           @RequestPart(value = "file3", required = false) List<MultipartFile> file4,
                       @ApiParam(value = "Hashtags", required = true) @RequestParam(required = false) String threadHashtags,
                       HttpSession session) {
//        System.out.println("========="+files0.get(0).getSize());
//        System.out.println("----------"+files1.get(0).getSize());








        if (session.getAttribute("userId") == null) {
            throw new UserNotLoginException();
        }
        if (categoryId == null || title == null || title.isEmpty() || content == null || content.isEmpty()) {
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

        thread.setUserId(getUserIdFromSession(session));
        if (!(files.get(0).isEmpty())) {
            String paths = "";
            for (String path : storageService.uploadToS3(files, session)) {
                paths += path + "▲";
            }
            thread.setPicture(paths.substring(0, paths.length() - 1));
        }
        //
        threadService.handleHashtags(threadHashtags, thread);
        // 保存Thread与Hashtag关联记录
       // threadService.saveThreadHashtags(thread);
        boolean flag = threadService.save(thread);
        if (flag){
            threadService.saveThreadHashtags(thread);
        }
        return new Result(flag ? Code.SAVE_OK : Code.SAVE_ERR, thread, flag ? "論壇文章新增成功" : "論壇文章新增失敗");
    }

    //移除收藏文章
    @DeleteMapping("/favorite/{favoriteThreadId}")
    @ApiOperation(value = "移除收藏文章OK", notes = "移除使用者收藏的文章")
    public Result removeFavoriteThread(@ApiParam(value = "文章 ID", required = true) @PathVariable Integer favoriteThreadId ,
                                       HttpSession session) {
        if (session.getAttribute("userId") == null) {
            throw new UserNotLoginException();
        }
        Integer userId = getUserIdFromSession(session);
        boolean flag = favoriteThreadService.removeById(favoriteThreadId);
        return new Result(flag ? Code.DELETE_OK : Code.DELETE_ERR, flag, flag ? "移除收藏文章成功" : "移除收藏文章失敗");
    }
    //修改文章--------------------------------------------------------------
    @PutMapping
    @ApiOperation("修改論壇文章")
    public Result updateById(
            Thread thread,
            @ApiParam(value = "文章標題", required = true) @RequestParam String title,
            @ApiParam(value = "文章內容", required = true) @RequestParam String content,
            @ApiParam(value = "文章分類 ID", required = true) @RequestParam Integer categoryId,
            @ApiParam(value = "檔案", required = false)
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            HttpSession session) {
        if (session.getAttribute("userId") == null) {
            throw new UserNotLoginException();
        }
        if(categoryId==null
                ||title==null||title.isEmpty()
                ||content==null||content.isEmpty()) {
            throw new ThreadColumnIsNullException();
        }
        if(!(files.get(0).isEmpty())) {
            String paths="";
            for (String path : storageService.uploadToS3(files, session)) {
                paths += path + "|";
            }
            thread.setPicture(paths.substring(0,paths.length()-1));
        }
        boolean flag = threadService.updateById(thread);
        return new Result(flag ? Code.UPDATE_OK : Code.UPDATE_ERR, flag, flag ?"論壇文章修改成功":"論壇文章修改失敗");
    }
    //切換使用者按讚--------------------------------------------------------------
    @PutMapping("/toggleUserLove/{threadId}")
    @ApiOperation("使用者按讚+紀錄OK")
    public Result toggleUserLove(@PathVariable Integer threadId, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            throw new UserNotLoginException();
        }
        Integer userId = (Integer) session.getAttribute("userId");
        int userLoveStatus = userThreadLoveService.toggleLove(userId, threadId);
        // 取得當前文章的按讚數
        Thread thread = threadService.getById(threadId);
        int loveCount = thread.getLove();
        // 根據按讚狀態回傳不同的訊息
        String message = userThreadLoveService.getLoveMessage(userLoveStatus);
        return new Result(Code.UPDATE_OK, thread , message);
    }
    //按讚--------------------------------------------------------------
    @PutMapping("/love/{threadId}")
    @ApiOperation("按讚OK")
    public Result love(@PathVariable Integer threadId){
        Thread thread = threadService.getById(threadId);
        threadService.love(thread);
        boolean flag = threadService.updateById(thread);
        return new Result(flag ? Code.UPDATE_OK : Code.UPDATE_ERR, flag, flag ?"論壇文章點讚成功":"論壇文章點讚失敗");
    }
    //取消按讚--------------------------------------------------------------
    @PutMapping("/cancelLove/{threadId}")
    @ApiOperation("取消按讚OK")
    public Result cancelLove(@PathVariable Integer threadId){
        Thread thread = threadService.getById(threadId);
        threadService.cancelLove(thread);
        boolean flag = threadService.updateById(thread);
        return new Result(flag ? Code.UPDATE_OK : Code.UPDATE_ERR, flag, flag ?"論壇文章點讚成功":"論壇文章點讚失敗");
    }
    //按讚+收回--------------------------------------------------------------
    @PutMapping("/toggleLove/{threadId}")
    @ApiOperation("按讚+收回OK")
    public Result toggleLove(@PathVariable Integer threadId) {
        Thread thread = threadService.getById(threadId);
        threadService.toggleLove(thread);
        boolean flag = threadService.updateById(thread);
        String message = (thread.getLove() % 2 == 0) ? "論壇文章取消按讚成功" : "論壇文章點讚成功";
        return new Result(flag ? Code.UPDATE_OK : Code.UPDATE_ERR, flag, message);
    }
    //查詢使用者收藏的文章--------------------------------------------------------------
    @GetMapping("/favorite/{userId}")
    @ApiOperation(value = "查詢使用者收藏的文章", notes = "查詢使用者收藏的文章")
    public Result getFavoriteThreads(@PathVariable Integer userId ,HttpSession session) {
       // Integer userId=getUserIdFromSession(session)
        if (session.getAttribute("userId") == null) {
            throw new UserNotLoginException();
        }
        if(userId==0){
         userId = getUserIdFromSession(session);
        }
        List<Thread> threads = threadService.getFavoriteThreads(userId);
        return threads.isEmpty() ? new Result(Code.GET_ERR, null, "查詢收藏文章失敗") : new Result(Code.GET_OK, threads, "查詢收藏文章成功");
    }

    //熱門文章--------------------------------------------------------------
    @GetMapping("/hotThread")
    @ApiOperation("熱門文章")
    public Result hotUser(HttpSession session) {
        List<Thread> thread = threadService.hotThread();
        Integer code = thread != null ? Code.GET_OK : Code.GET_ERR;
        String msg = thread != null ? "查詢熱門文章資料成功" : "查無資料";
        return new Result(code, thread, msg);
    }



    //查詢主題文章--------------------------------------------------------------
    @ApiImplicitParam(name = "查詢主題文章", value = "論壇文章Id")
    @GetMapping("category/{categoryId}")
    @ApiOperation("查詢主題文章")
    public Result getCategoryThread(@PathVariable Integer categoryId) {
        List<Thread> thread = threadService.getCategoryThread(categoryId);
        Integer code = thread != null ? Code.GET_OK : Code.GET_ERR;
        String msg = thread != null ? "論壇主題文章資料取得成功" : "查無資料";
        return new Result(code, thread, msg);
    }
    //查詢單筆論壇文章--------------------------------------------------------------
    @ApiImplicitParam(name = "threadId", value = "論壇文章Id")
    @GetMapping("/{threadId}")
    @ApiOperation("查詢單筆論壇文章")
    public Result getUserById(@PathVariable Integer threadId) {
        Thread thread = threadService.getThreadWithCategoryName(threadId);
        Integer code = thread != null ? Code.GET_OK : Code.GET_ERR;
        String msg = thread != null ? "論壇文章資料取得成功" : "查無資料";
        return new Result(code, thread, msg);
    }
    //查詢最後一筆論壇文章--------------------------------------------------------------
    @GetMapping("last")
    @ApiOperation("查詢最後一筆論壇文章")
    public Result getUserlastById() {
        //Integer getThreadId = threadService.getLastThreadById();
        Thread thread = threadService.getLastThreadWithDetails();
        Integer code = thread != null ? Code.GET_OK : Code.GET_ERR;
        String msg = thread != null ? "最後一筆資料取得成功" : "查無資料";
        return new Result(code, thread, msg);
    }
    //關鍵字搜尋--------------------------------------------------------------
    @GetMapping("/search")
    @ApiOperation("關鍵字搜尋")
    public Result searchThreadsByKeyword(@ApiParam("關鍵字") @RequestParam String keyword) {
        return threadService.searchThreads(keyword);
    }



}
