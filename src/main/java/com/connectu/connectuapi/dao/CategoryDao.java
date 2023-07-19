package com.connectu.connectuapi.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.connectu.connectuapi.domain.Category;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CategoryDao extends MPJBaseMapper<Category> {

}
