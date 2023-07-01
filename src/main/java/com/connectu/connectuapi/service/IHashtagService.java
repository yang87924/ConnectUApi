package com.connectu.connectuapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.Hashtag;

import java.util.List;

public interface IHashtagService extends IService<Hashtag> {
    List<Hashtag> getTopThreeHashtags();
}