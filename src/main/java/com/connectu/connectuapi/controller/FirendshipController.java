package com.connectu.connectuapi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.connectu.connectuapi.controller.util.Code;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.domain.Friendship;
import com.connectu.connectuapi.domain.Reply;
import com.connectu.connectuapi.exception.UserNotLoginException;
import com.connectu.connectuapi.service.IFriendshipService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
@Api(tags ="好友")
@RestController
@RequestMapping("/firendship")
public class FirendshipController extends BaseController{
    @Autowired
    private IFriendshipService iFriendshipService;
    @GetMapping()
    @ApiImplicitParam(name = "userId", value = "論壇文章id")
    @ApiOperation("所有追隨的人的文章")
    public Result getFirendShipThread(HttpSession session, @RequestParam(defaultValue = "1") Integer page) {
        if (session.getAttribute("userId") == null) {
            throw new UserNotLoginException();
        }

        // 设置分页参数
        Page<Friendship> friendshipPage = new Page<>(page, 4);

        List<Friendship> friendships = iFriendshipService.getFirendShipThread(getUserIdFromSession(session), friendshipPage);

        Integer code = friendships != null ? Code.GET_OK : Code.GET_ERR;
        String msg = friendships != null ? "所有追隨的人的文章" : "查無追隨的人的文章";
        return new Result(code, friendships, msg);
    }
}
