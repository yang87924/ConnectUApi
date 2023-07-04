package com.connectu.connectuapi.service;

import com.connectu.connectuapi.domain.DyHashtag;
import com.connectu.connectuapi.domain.Hashtag;
import com.github.yulichang.base.MPJBaseService;

import java.util.List;

public interface IDyHashtagService extends MPJBaseService<DyHashtag> {
    List<DyHashtag> getTopDyThreeHashtags();
}