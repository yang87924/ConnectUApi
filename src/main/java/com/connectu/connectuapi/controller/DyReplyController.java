package com.connectu.connectuapi.controller;

import com.connectu.connectuapi.controller.util.Code;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.domain.DyReply;
import com.connectu.connectuapi.domain.Reply;
import com.connectu.connectuapi.service.IDyReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags ="動態文章留言")
@RestController
@RequestMapping("/dyReply")
@CrossOrigin(origins = "*")
public class DyReplyController {
    @Autowired
    private IDyReplyService dyReplyService;
    //假資料

//    @ApiIgnore    // 忽略这个api
//   // @PostMapping("/addFakeDyReply")
//    public String addFakeDyThreads() {
//        dyReplyService.addFakeDyReply(50);
//        return "Fake DyReply added successfully!";
//    }
    //取得動態文章的所有留言
    @GetMapping("/{DyThreadid}")
    @ApiImplicitParam(name = "DyThreadid", value = "動態文章id")
    @ApiOperation("取得動態文章的所有留言")
    public Result getUserThread(@PathVariable Integer DyThreadid) {
        List<DyReply> reply = dyReplyService.getDyThreadReplyById(DyThreadid);
        Integer code = reply != null ? Code.GET_OK : Code.GET_ERR;
        String msg = reply != null ? "查詢動態文章所有留言資料成功" : "查無動態文章留言資料";
        return new Result(code, reply, msg);
    }
    //修改動態文章留言
    @PutMapping
    @ApiOperation("修改動態文章留言")
    public Result updateById(@RequestBody DyReply dyreply) {
        boolean flag = dyReplyService.updateById(dyreply);
        return new Result(flag ? Code.UPDATE_OK : Code.UPDATE_ERR, flag, flag ?"動態文章留言更新成功":"動態文章留言更新失敗");
    }
    //刪除動態文章留言
    @DeleteMapping("/{DyThreadid}")
    @ApiImplicitParam(name = "DyThreadid", value = "動態文章id")
    @ApiOperation("刪除動態文章留言")
    public Result deleteById(@PathVariable Integer DyThreadid) {
        boolean flag = dyReplyService.removeById(DyThreadid);
        return new Result(flag ? Code.DELETE_OK : Code.DELETE_ERR, flag, flag ?"動態文章留言刪除成功":"動態文章留言刪除失敗");
    }
    //新增動態論壇留言
    @PostMapping
    @ApiOperation("新增動態文章留言")
    public Result save(@RequestBody DyReply dyreply) {
        boolean flag = dyReplyService.save(dyreply);
        return new Result(flag ? Code.SAVE_OK : Code.SAVE_ERR, flag, flag ?"動態文章留言新增成功":"動態文章留言新增失敗");
    }
}
