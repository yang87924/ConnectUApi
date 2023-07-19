package com.connectu.connectuapi.dao;


import com.connectu.connectuapi.domain.Friendship;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FriendshipDao extends MPJBaseMapper<Friendship> {

}
