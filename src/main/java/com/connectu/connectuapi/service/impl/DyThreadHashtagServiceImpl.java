package com.connectu.connectuapi.service.impl;

import com.connectu.connectuapi.dao.DyHashtagDao;
import com.connectu.connectuapi.dao.DyThreadHashtagDao;
import com.connectu.connectuapi.domain.DyHashtag;
import com.connectu.connectuapi.domain.dyThreadHashtag;
import com.connectu.connectuapi.service.IDyHashtagService;
import com.connectu.connectuapi.service.IDyThreadHashtagService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DyThreadHashtagServiceImpl extends MPJBaseServiceImpl<DyThreadHashtagDao, dyThreadHashtag> implements IDyThreadHashtagService {
}
