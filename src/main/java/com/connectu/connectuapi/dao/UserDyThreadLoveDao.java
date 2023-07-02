package com.connectu.connectuapi.dao;

import com.connectu.connectuapi.domain.UserDyThreadLove;
import com.connectu.connectuapi.domain.UserThreadLove;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDyThreadLoveDao extends MPJBaseMapper<UserDyThreadLove> {
    void toggleLove();
}
