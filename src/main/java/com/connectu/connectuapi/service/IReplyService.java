package com.connectu.connectuapi.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.Reply;
import com.connectu.connectuapi.domain.Thread;
import com.github.yulichang.base.MPJBaseService;

import java.util.List;

public interface IReplyService extends MPJBaseService<Reply> {
    void addFakeReply(int count);
    List<Reply> getThreadReplyById(int id);
}
