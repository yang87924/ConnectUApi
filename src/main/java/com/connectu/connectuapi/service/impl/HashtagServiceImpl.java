package com.connectu.connectuapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.HashtagDao;
import com.connectu.connectuapi.domain.Hashtag;
import com.connectu.connectuapi.service.IHashtagService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HashtagServiceImpl extends MPJBaseServiceImpl<HashtagDao, Hashtag> implements IHashtagService {
    @Autowired
    private HashtagDao hashtagDao;

    // 其他方法实现...

    @Override
    public List<Hashtag> getHashtagsByThreadId(Integer threadId) {
        // 根据 threadId 查询对应的 Hashtag 列表
        QueryWrapper<Hashtag> wrapper = new QueryWrapper<>();
        wrapper.eq("threadId", threadId);
        return hashtagDao.selectList(wrapper);
    }
    @Override
    public List<Hashtag> getTopThreeHashtags() {
        QueryWrapper<Hashtag> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("amount");
        queryWrapper.last("LIMIT 3");
        return list(queryWrapper);
    }
}
