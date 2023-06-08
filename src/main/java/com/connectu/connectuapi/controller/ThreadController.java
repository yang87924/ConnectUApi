package com.connectu.connectuapi.controller;

import com.connectu.connectuapi.controller.util.Code;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.service.IThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/threads")
public class ThreadController {
    @Autowired
    private IThreadService threadService;
    @PostMapping("/addFakeThread")
    public String addFakeThread() {
        threadService.addFakeThread(50);
        return "Fake Thread added successfully!";
    }
    //新增文章
    @PostMapping
    public Result save(@RequestBody Thread thread) {
        System.out.println(thread);
        boolean flag = threadService.save(thread);
        return new Result(flag ? Code.SAVE_OK : Code.SAVE_ERR, flag, flag ?"用戶創建成功":"用戶創建失敗");
    }
    //查詢所有無章
    @GetMapping
    public Result getAllThread() {
        List<Thread> thread = threadService.list(null);
        Integer code = thread != null ? Code.GET_OK : Code.GET_ERR;
        String msg = thread != null ? "所有文章資料成功" : "查無文章資料";
        return new Result(code, thread, msg);
    }
    //修改文章
    @PutMapping
    public Result updateById(@RequestBody Thread thread) {
        boolean flag = threadService.updateById(thread);
        return new Result(flag ? Code.UPDATE_OK : Code.UPDATE_ERR, flag, flag ?"用戶資料更新成功":"用戶資料更新失敗");
    }
}
