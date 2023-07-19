package com.connectu.connectuapi.dao;

import com.connectu.connectuapi.domain.User;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Mapper
public interface UserDao extends MPJBaseMapper<User> {

//    @Select("SELECT * FROM thread ORDER BY love + favoriteCount DESC")
//    List<Thread> getSortedThreads();
}
