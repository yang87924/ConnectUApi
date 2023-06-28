package com.connectu.connectuapi.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.connectu.connectuapi.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserDao extends BaseMapper<User> {
    @Select("SELECT * FROM thread ORDER BY love + favoriteCount DESC")
    List<Thread> getSortedThreads();
}
