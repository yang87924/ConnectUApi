package com.connectu.connectuapi.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.Reply;
import com.connectu.connectuapi.domain.Thread;

import java.util.List;

public interface IReplyService extends IService<Reply> {
    void addFakeReply(int count);
    List<Reply> getThreadReplyById(int id);
}
