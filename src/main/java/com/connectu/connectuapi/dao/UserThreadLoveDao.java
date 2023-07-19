package com.connectu.connectuapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.connectu.connectuapi.domain.UserThreadLove;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserThreadLoveDao extends MPJBaseMapper<UserThreadLove> {
    void toggleLove();
}
