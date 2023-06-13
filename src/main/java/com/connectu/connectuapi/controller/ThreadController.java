package com.connectu.connectuapi.controller;

import com.connectu.connectuapi.controller.util.Code;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.domain.User;
import com.connectu.connectuapi.exception.file.*;
import com.connectu.connectuapi.service.IReplyService;
import com.connectu.connectuapi.service.IThreadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.connectu.connectuapi.service.utils.faker.getSystemTime;

@Api(tags ="論壇")
@RestController
@RequestMapping("/threads")
public class ThreadController extends BaseController{
    @Autowired
    private IThreadService threadService;
    //假資料
    @ApiIgnore    // 忽略这个api
    @PostMapping("/addFakeThread")
    public String addFakeThread() {
        threadService.addFakeThread(50);
        return "Fake Thread added successfully!";
    }

    @PostMapping
    @ApiOperation("新增論壇文章")
    public Result save(Thread thread, List<MultipartFile> files, HttpSession session) {
        if(!(files.get(0).isEmpty())) {
            String paths="";
            for (String path : upload(files, session)) {
                paths += path + "|";
            }
            thread.setPicture(paths.substring(0,paths.length()-1));
        }
        boolean flag = threadService.save(thread);
        return new Result(flag ? Code.SAVE_OK : Code.SAVE_ERR, flag, flag ?"論壇文章新增成功":"論壇文章新增失敗");
    }
    //查詢所有文章
    @GetMapping
    @ApiOperation("查詢所有論壇文章")
    public Result getAllThread() {
        List<Thread> thread = threadService.list();
        Integer code = thread != null ? Code.GET_OK : Code.GET_ERR;
        String msg = thread != null ? "所有論壇文章資料成功" : "查無論壇文章資料";
        return new Result(code, thread, msg);
    }

    //修改文章
    @PutMapping
    @ApiOperation("修改論壇文章")
    public Result updateById(Thread thread, List<MultipartFile> files, HttpSession session) {
        if(!(files.get(0).isEmpty())) {
            String paths="";
            for (String path : upload(files, session)) {
                paths += path + "|";
            }
            thread.setPicture(paths.substring(0,paths.length()-1));
        }
        boolean flag = threadService.updateById(thread);
        return new Result(flag ? Code.SAVE_OK : Code.SAVE_ERR, flag, flag ?"論壇文章修改成功":"論壇文章修改失敗");
    }
    //刪除文章
    @ApiImplicitParam(name = "threadId", value = "論壇文章id")
    @DeleteMapping("/{threadId}")
    @ApiOperation("刪除論壇文章")
    public Result deleteById(@PathVariable Integer threadId) {
        boolean flag = threadService.removeById(threadId);
        return new Result(flag ? Code.DELETE_OK : Code.DELETE_ERR, flag, flag ?"論壇文章刪除成功":"論壇文章刪除失敗");
    }
    //查詢單筆論壇
    @GetMapping("/{threadId}")
    @ApiOperation("查詢單筆論壇文章")
    public Result getUserById(@PathVariable Integer threadId) {
        Thread thread = threadService.getThreadWithCategoryName(threadId);
        Integer code = thread != null ? Code.GET_OK : Code.GET_ERR;
        String msg = thread != null ? "論壇文章資料取得成功" : "查無資料";
        return new Result(code, thread, msg);
    }
    //查詢最後一筆論壇文章
    @GetMapping("last")
    @ApiOperation("查詢最後一筆論壇文章")
    public Result getUserlastById() {
        Integer getThreadId = threadService.getLastThreadById();
        Thread thread = threadService.getThreadWithCategoryName(getThreadId - 1);
        Integer code = thread != null ? Code.GET_OK : Code.GET_ERR;
        String msg = thread != null ? "最後一筆資料取得成功" : "查無資料";
        return new Result(code, thread, msg);
    }
    //取得使用者的所有文章
    @GetMapping("user/{id}")
    @ApiOperation("取得使用者的所有論壇文章")
    public Result getUserThread(@PathVariable Integer id) {
        List<Thread> thread = threadService.getUserThreadById(id);
        Integer code = thread != null ? Code.GET_OK : Code.GET_ERR;
        String msg = thread != null ? "查詢使用者論壇文章資料成功" : "查無論壇文章資料";
        return new Result(code, thread, msg);
    }
    @GetMapping("/search")
    @ApiOperation("關鍵字搜尋")
    public Result searchThreadsByKeyword(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryName) {
        List<Thread> search = null;
        if (keyword != null && !keyword.isEmpty() && categoryName != null && !categoryName.isEmpty()) {
            search = threadService.searchThreadsByKeyword(keyword, categoryName);
        }
        Integer code = search != null && !search.isEmpty() ? Code.GET_OK : Code.GET_ERR;
        String msg = search != null && !search.isEmpty() ? "搜尋文章資料成功" : "搜尋文章資料失敗!請重新輸入關鍵字";
        return new Result(code, search, msg);
    }
}
