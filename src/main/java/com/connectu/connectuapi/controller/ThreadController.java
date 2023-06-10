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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Api(tags ="論壇")
@RestController
@RequestMapping("/threads")
public class ThreadController extends BaseController{
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
    public Result save(@RequestBody Thread thread ,HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId"); // 將類型從 String 修改為 Integer
        ///System.out.println(userId);
        boolean flag = threadService.saveThread(thread,userId);
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

    //修改文章
    @PutMapping
    @ApiOperation("修改論壇文章")
    public Result updateById(@RequestBody Thread thread) {
        boolean flag = threadService.updateById(thread);
        return new Result(flag ? Code.UPDATE_OK : Code.UPDATE_ERR, flag, flag ?"論壇文章更新成功":"論壇文章更新失敗");
    }
    //刪除文章
    @ApiImplicitParam(name = "Threadid", value = "論壇文章id")
    @DeleteMapping("/{Threadid}")
    @ApiOperation("刪除論壇文章")
    public Result deleteById(@PathVariable Integer Threadid) {
        boolean flag = threadService.removeById(Threadid);
        return new Result(flag ? Code.DELETE_OK : Code.DELETE_ERR, flag, flag ?"論壇文章刪除成功":"論壇文章刪除失敗");
    }

    public static final int AVATAR_MAX_SIZE = 10 * 1024 * 1024;

    public static final List<String> AVATAR_TYPES = new ArrayList<>();

    static {
        AVATAR_TYPES.add("image/jpeg");
        AVATAR_TYPES.add("image/png");
        AVATAR_TYPES.add("image/bmp");
        AVATAR_TYPES.add("image/gif");
    }
//更新用戶頭像--------------------------------------------------------------
    @PostMapping("/upload")
    @ApiOperation("更改用戶頭像")
    public Result changeAvatar(@RequestParam("file") MultipartFile file, HttpSession session) {
        String a=upload(file,session);
        System.out.println(a);
//        User loginUser = userService.getById(getUserIdFromSession(session));
//        String revisedPath = avatar.replace("\\", "/");
//        loginUser.setAvatar(revisedPath);
//        userService.updateById(loginUser);

        return new Result(Code.SAVE_OK, "revisedPath", "頭像存取成功");
    }
}
