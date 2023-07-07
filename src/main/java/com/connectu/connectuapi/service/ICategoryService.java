package com.connectu.connectuapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.connectu.connectuapi.domain.Category;
import com.connectu.connectuapi.domain.DyReply;
import com.connectu.connectuapi.domain.Thread;
import com.github.yulichang.base.MPJBaseService;

import java.util.List;

public interface ICategoryService extends MPJBaseService<Category> {
    List<Category> getCategoriesWithThreadCount();
}
