package com.connectu.connectuapi.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.connectu.connectuapi.domain.Category;
import com.connectu.connectuapi.domain.Thread;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ThreadDao extends BaseMapper<Thread> {
    @Select("SELECT t.*, c.categoryName FROM thread t JOIN category c ON t.categoryId = c.categoryId")
    List<Thread> selectAllWithCategoryName();
}
