package com.connectu.connectuapi.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.connectu.connectuapi.domain.UserThreadLove;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserThreadLoveDao extends BaseMapper<UserThreadLove> {
    void toggleLove();
}
