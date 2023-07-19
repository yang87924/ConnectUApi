package com.connectu.connectuapi.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.DyReply;
import com.github.yulichang.base.MPJBaseService;

import java.util.List;

public interface IDyReplyService extends MPJBaseService<DyReply> {
    void addFakeDyReply(int count);
    List<DyReply> getDyThreadReplyById(int id);
}
