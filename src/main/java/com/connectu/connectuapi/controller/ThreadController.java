package com.connectu.connectuapi.controller;

import com.connectu.connectuapi.controller.util.Code;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.service.IReplyService;
import com.connectu.connectuapi.service.IThreadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags ="論壇")
@RestController
@RequestMapping("/threads")
public class ThreadController {
    @Autowired
    private IThreadService threadService;
    @Autowired
    private IReplyService replyService;

    //假資料
    @ApiIgnore    // 忽略这个api
    @PostMapping("/addFakeThread")
    public String addFakeThread() {
        threadService.addFakeThread(50);
        return "Fake Thread added successfully!";
    }
    //新增論壇文章
    @PostMapping
    @ApiOperation("新增論壇文章")
    public Result save(@RequestBody Thread thread) {
        //System.out.println(thread);
        boolean flag = threadService.save(thread);
        return new Result(flag ? Code.SAVE_OK : Code.SAVE_ERR, flag, flag ?"論壇文章新增成功":"論壇文章新增失敗");
    }
    //查詢所有文章
    @GetMapping
    @ApiOperation("查詢所有論壇文章")
    public Result getAllThread() {
        List<Thread> thread = threadService.list(null);
        Integer code = thread != null ? Code.GET_OK : Code.GET_ERR;
        String msg = thread != null ? "所有論壇文章資料成功" : "查無論壇文章資料";
        return new Result(code, thread, msg);
    }
    //取得使用者的所有文章
    @GetMapping("/{id}")
    @ApiOperation("取得使用者的所有論壇文章")
    public Result getUserThread(@PathVariable Integer id) {
        List<Thread> thread = threadService.getUserThreadById(id);
        Integer code = thread != null ? Code.GET_OK : Code.GET_ERR;
        String msg = thread != null ? "查詢使用者論壇文章資料成功" : "查無論壇文章資料";
        return new Result(code, thread, msg);
    }
    //修改文章
    @PutMapping
    @ApiOperation("修改論壇文章")
    public Result updateById(@RequestBody Thread thread) {
        boolean flag = threadService.updateById(thread);
        return new Result(flag ? Code.UPDATE_OK : Code.UPDATE_ERR, flag, flag ?"論壇文章更新成功":"論壇文章更新失敗");
    }
    //刪除文章
    @DeleteMapping("/{id}")
    @ApiOperation("刪除論壇文章")
    public Result deleteById(@PathVariable Integer id) {
        boolean flag = threadService.removeById(id);
        return new Result(flag ? Code.DELETE_OK : Code.DELETE_ERR, flag, flag ?"論壇文章刪除成功":"論壇文章刪除失敗");
    }
}
