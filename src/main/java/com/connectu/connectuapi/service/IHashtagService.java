package com.connectu.connectuapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.Hashtag;
import com.github.yulichang.base.MPJBaseService;

import java.util.List;

public interface IHashtagService extends MPJBaseService<Hashtag> {
    List<Hashtag> getTopThreeHashtags();
    List<Hashtag> getHashtagsByThreadId(Integer threadId);
}