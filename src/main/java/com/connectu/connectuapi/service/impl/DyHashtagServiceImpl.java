package com.connectu.connectuapi.service.impl;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.connectu.connectuapi.dao.DyHashtagDao;
import com.connectu.connectuapi.domain.DyHashtag;
import com.connectu.connectuapi.service.IDyHashtagService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Service
public class DyHashtagServiceImpl extends MPJBaseServiceImpl<DyHashtagDao, DyHashtag>implements IDyHashtagService  {
    @Override
    public List<DyHashtag> getTopDyThreeHashtags() {
        QueryWrapper<DyHashtag> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("amount");
        queryWrapper.last("LIMIT 5");
        return list(queryWrapper);
    }
}
