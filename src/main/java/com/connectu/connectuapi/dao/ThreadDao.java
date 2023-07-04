package com.connectu.connectuapi.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.connectu.connectuapi.domain.Category;
import com.connectu.connectuapi.domain.Thread;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface ThreadDao extends MPJBaseMapper<Thread> {
    @Select("SELECT t.* FROM thread t JOIN favoriteThread ft ON t.threadId = ft.threadId WHERE ft.userId = #{userId}")
    List<Thread> getFavoriteThreads(@Param("userId") Integer userId);
}