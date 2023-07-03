package com.connectu.connectuapi.controller;

import com.connectu.connectuapi.controller.util.Code;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.domain.Reply;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.service.IReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(tags ="論壇留言")
@RestController
@RequestMapping("/replys")
public class ReplyController {
    @Autowired
    private IReplyService replyService;
    //假資料
    @ApiIgnore    // 忽略这个api
    @PostMapping("/addFakeReply")
    public String addFakeReply() {
        replyService.addFakeReply(500);
        return "Fake Reply added successfully!";
    }
    //取得論壇文章的所有留言
    @GetMapping("/{Threadid}")
    @ApiImplicitParam(name = "Threadid", value = "論壇文章id")
    @ApiOperation("取得論壇文章的所有流言")
    public Result getUserThread(@PathVariable Integer Threadid) {
        List<Reply> reply = replyService.getThreadReplyById(Threadid);
        Integer code = reply != null ? Code.GET_OK : Code.GET_ERR;
        String msg = reply != null ? "查詢使用者論壇文章資料成功" : "查無論壇文章資料";
        return new Result(code, reply, msg);
    }
    //修改文章留言
    @PutMapping
    @ApiOperation("修改文章留言")
    public Result updateById(@RequestBody Reply reply) {
        boolean flag = replyService.updateById(reply);
        return new Result(flag ? Code.UPDATE_OK : Code.UPDATE_ERR, flag, flag ?"論壇文章留言更新成功":"論壇文章留言更新失敗");
    }
    //刪除論壇文章留言
    @DeleteMapping("/{id}")
    @ApiOperation("刪除論壇文章留言")
    public Result deleteById(@PathVariable Integer id) {
        boolean flag = replyService.removeById(id);
        return new Result(flag ? Code.DELETE_OK : Code.DELETE_ERR, flag, flag ?"論壇文章刪除成功":"論壇文章刪除失敗");
    }
    //新增論壇留言
    @PostMapping
    @ApiOperation("新增論壇留言")
    public Result save(@RequestBody Reply reply) {
        //System.out.println(thread);
        boolean flag = replyService.save(reply);
        return new Result(flag ? Code.SAVE_OK : Code.SAVE_ERR, flag, flag ?"論壇文章新增成功":"論壇文章新增失敗");
    }
}
