package com.connectu.connectuapi.service.impl;

import com.connectu.connectuapi.dao.HashtagDao;
import com.connectu.connectuapi.dao.ThreadHashtagDao;
import com.connectu.connectuapi.domain.Hashtag;
import com.connectu.connectuapi.domain.ThreadHashtag;
import com.connectu.connectuapi.service.IHashtagService;
import com.connectu.connectuapi.service.IThreadHashtagService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ThreadHashtagServiceImpl extends MPJBaseServiceImpl<ThreadHashtagDao, ThreadHashtag> implements IThreadHashtagService {
}
