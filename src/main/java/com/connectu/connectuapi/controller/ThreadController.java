package com.connectu.connectuapi.controller;

import com.connectu.connectuapi.controller.util.Code;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.domain.User;
import com.connectu.connectuapi.domain.UserThreadLove;
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


    @GetMapping("/getUserSort")
    @ApiOperation(value = "使用者的所有文章(排序)")
    public Result sortThreads() {
        List<Thread> threads = threadService.hotThread();
        return threads.isEmpty() ? new Result(Code.GET_ERR, null, "查詢熱門文章失敗") : new Result(Code.GET_OK, threads, "查詢熱門文章成功");    }
    //新增收藏文章
    @PostMapping("/favorite")
    @ApiOperation(value = "新增收藏文章", notes = "新增使用者收藏的文章")
    public Result addFavoriteThread(@ApiParam(value = "文章 ID", required = true) @RequestParam Integer threadId,
                                    HttpSession session) {
        if (session.getAttribute("userId") == null) {
            throw new UserNotLoginException();
        }
        Integer userId = getUserIdFromSession(session);
        boolean flag = threadService.addFavoriteThread(userId, threadId);
        return new Result(flag ? Code.SAVE_OK : Code.SAVE_ERR, flag, flag ? "收藏文章成功" : "收藏文章失敗");
    }
    //新增文章--------------------------------------------------------------
    @PostMapping
    @ApiOperation(value = "新增論壇文章", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result save(  Thread thread,@ApiParam(value = "文章標題", required = true) @RequestParam String title,
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
        thread.setUserId(getUserIdFromSession(session));
        if(!(files.get(0).isEmpty())) {
            String paths="";
            for (String path : storageService.uploadToS3(files, session)) {
                paths += path + "|";
            }
            thread.setPicture(paths.substring(0,paths.length()-1));
        }
        boolean flag = threadService.save(thread);
        return new Result(flag ? Code.SAVE_OK : Code.SAVE_ERR, flag, flag ? "論壇文章新增成功" : "論壇文章新增失敗");
    }
    //刪除文章--------------------------------------------------------------
    @ApiImplicitParam(name = "threadId", value = "論壇文章Id")
    @DeleteMapping("/{threadId}")
    @ApiOperation("刪除論壇文章")
    public Result deleteById(@PathVariable Integer threadId) {
        boolean flag = threadService.removeById(threadId);
        return new Result(flag ? Code.DELETE_OK : Code.DELETE_ERR, flag, flag ?"論壇文章刪除成功":"論壇文章刪除失敗");
    }
    //移除收藏文章
    @DeleteMapping("/favorite/{favoriteThreadId}")
    @ApiOperation(value = "移除收藏文章", notes = "移除使用者收藏的文章")
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
    @ApiOperation("切換使用者按讚")
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
    @ApiOperation("按讚")
    public Result love(@PathVariable Integer threadId){
        Thread thread = threadService.getById(threadId);
        threadService.love(thread);
        boolean flag = threadService.updateById(thread);
        return new Result(flag ? Code.UPDATE_OK : Code.UPDATE_ERR, flag, flag ?"論壇文章點讚成功":"論壇文章點讚失敗");
    }
    //取消按讚--------------------------------------------------------------
    @PutMapping("/cancelLove/{threadId}")
    @ApiOperation("取消按讚")
    public Result cancelLove(@PathVariable Integer threadId){
        Thread thread = threadService.getById(threadId);
        threadService.cancelLove(thread);
        boolean flag = threadService.updateById(thread);
        return new Result(flag ? Code.UPDATE_OK : Code.UPDATE_ERR, flag, flag ?"論壇文章點讚成功":"論壇文章點讚失敗");
    }
    //切換按讚--------------------------------------------------------------
    @PutMapping("/toggleLove/{threadId}")
    @ApiOperation("切換按讚")
    public Result toggleLove(@PathVariable Integer threadId) {
        Thread thread = threadService.getById(threadId);
        threadService.toggleLove(thread);
        boolean flag = threadService.updateById(thread);
        String message = (thread.getLove() % 2 == 0) ? "論壇文章取消按讚成功" : "論壇文章點讚成功";
        return new Result(flag ? Code.UPDATE_OK : Code.UPDATE_ERR, flag, message);
    }
    //亂數按讚--------------------------------------------------------------
    @PutMapping("/loveRandom")
    @ApiOperation("按讚亂數")
    public void loveRandom(){
        Set<Integer> idSet = new HashSet<>();
        Integer num=threadService.list().size();
        System.out.println(num);
//        while(idSet.size() < 182){
//            idSet.add((int) (Math.random() * 182) + 1);
//        }
//        for (Integer id : idSet){
//            Thread thread = threadService.getById(id);
//            if(thread != null){
//                Integer love = (int) (Math.random() * 1000) + 1;
//                Integer favorite = (int) (Math.random() * 1000) + 1;
//                thread.setLove(love);
//                thread.setFavoriteCount(favorite);
//                boolean flag = threadService.updateById(thread);
//            }
//        }
    }
    //查詢使用者收藏的文章
    @GetMapping("/favorite")
    @ApiOperation(value = "查詢使用者收藏的文章", notes = "查詢使用者收藏的文章")
    public Result getFavoriteThreads(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            throw new UserNotLoginException();
        }
        Integer userId = getUserIdFromSession(session);
        List<Thread> threads = threadService.getFavoriteThreads(userId);
        return threads.isEmpty() ? new Result(Code.GET_ERR, null, "查詢收藏文章失敗") : new Result(Code.GET_OK, threads, "查詢收藏文章成功");
    }
    //熱門作者--------------------------------------------------------------
    @GetMapping("/hotUser")
    @ApiOperation("熱門作者")
    public Result hotUser(HttpSession session) {
        Integer userId = getUserIdFromSession(session);
        List<Thread> thread = threadService.hotUser(userId);
        Integer code = thread != null ? Code.GET_OK : Code.GET_ERR;
        String msg = thread != null ? "查詢熱門使用者資料成功" : "查無資料";
        return new Result(code, thread, msg);
    }
    //查詢使用者的所有文章--------------------------------------------------------------
    @GetMapping("/userThread")
    @ApiOperation("查詢使用者的所有論壇文章")
    public Result getUserThread(HttpSession session) {
        Integer userId=getUserIdFromSession(session);
        List<Thread> thread = threadService.getUserThread(userId);
        Integer code = thread != null ? Code.GET_OK : Code.GET_ERR;
        String msg = thread != null ? "查詢使用者論壇文章資料成功" : "查無論壇文章資料";
        return new Result(code, thread, msg);
    }
    //查詢所有文章--------------------------------------------------------------
    @GetMapping
    @ApiOperation("查詢所有論壇文章")
    public Result getAllThread() {
        List<Thread> thread = threadService.list();
        thread.sort(Comparator.comparing(Thread::getCreatedAt, Comparator.reverseOrder())); // 根據 createdAt 欄位進行遞減排序
        Integer code = thread != null ? Code.GET_OK : Code.GET_ERR;
        String msg = thread != null ? "所有論壇文章資料成功" : "查無論壇文章資料";
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
        Integer getThreadId = threadService.getLastThreadById();
        Thread thread = threadService.getThreadWithCategoryName(getThreadId - 1);
        Integer code = thread != null ? Code.GET_OK : Code.GET_ERR;
        String msg = thread != null ? "最後一筆資料取得成功" : "查無資料";
        return new Result(code, thread, msg);
    }
    //關鍵字搜尋--------------------------------------------------------------
    @GetMapping("/search")
    @ApiOperation("關鍵字搜尋")
    public Result searchThreadsByKeyword(
            @ApiParam("關鍵字") @RequestParam String keyword,
            @ApiParam("分類名稱") @RequestParam String categoryName) {
        return threadService.searchThreads(keyword, categoryName);
    }
    //假資料
    @ApiIgnore    // 忽略这个api
    @PostMapping("/addFakeThread")
    public String addFakeThread() {
        threadService.addFakeThread(50);
        return "Fake Thread added successfully!";
    }

}
