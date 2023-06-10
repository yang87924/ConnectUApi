package com.connectu.connectuapi.controller;

import com.connectu.connectuapi.controller.util.Code;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.domain.DyThread;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.service.IDyThreadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags ="動態文章")
@RestController
@RequestMapping("/DyThreads")
public class DyThreadController {
    @Autowired
    private IDyThreadService dyThreadService;
    //假資料
    @ApiIgnore    // 忽略这个api
    @PostMapping("/addFakeDyThread")
    public String addFakeDyThreads() {
        dyThreadService.addFakeDyThread(50);
        return "Fake DyThreads added successfully!";
    }
    @PostMapping
    @ApiOperation("新增動態文章")
    public Result save(@RequestBody DyThread dyThread) {
        //System.out.println(thread);
        boolean flag = dyThreadService.save(dyThread);
        return new Result(flag ? Code.SAVE_OK : Code.SAVE_ERR, flag, flag ?"動態文章新增成功":"動態文章新增失敗");
    }
    //查詢動態所有文章
    @GetMapping
    @ApiOperation("查詢動態所有文章")
    public Result getAllDyThread() {
        List<DyThread> dythread = dyThreadService.list(null);
        Integer code = dythread != null ? Code.GET_OK : Code.GET_ERR;
        String msg = dythread != null ? "所有動態文章資料成功" : "查無動態文章資料";
        return new Result(code, dythread, msg);
    }

    //修改文章
    @PutMapping
    @ApiOperation("修改動態文章")
    public Result updateById(@RequestBody DyThread dyThread) {
        boolean flag = dyThreadService.updateById(dyThread);
        return new Result(flag ? Code.UPDATE_OK : Code.UPDATE_ERR, flag, flag ?"動態文章更新成功":"動態文章更新失敗");
    }
    //刪除文章
    @DeleteMapping("/{DyThreadid}")
    @ApiImplicitParam(name = "DyThreadid", value = "動態文章id")
    @ApiOperation("刪除動態文章")
    public Result deleteById(@PathVariable Integer DyThreadid) {
        boolean flag = dyThreadService.removeById(DyThreadid);
        return new Result(flag ? Code.DELETE_OK : Code.DELETE_ERR, flag, flag ?"動態文章刪除成功":"動態文章刪除失敗");
    }
}
