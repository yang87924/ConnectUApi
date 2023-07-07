package com.connectu.connectuapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.CategoryDao;
import com.connectu.connectuapi.dao.DyReplyDao;
import com.connectu.connectuapi.dao.ThreadDao;
import com.connectu.connectuapi.domain.*;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.service.ICategoryService;
import com.connectu.connectuapi.service.IDyReplyService;
import com.connectu.connectuapi.service.IThreadService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.JoinType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Transactional
@Service
public class CategoryServiceImpl extends MPJBaseServiceImpl<CategoryDao, Category> implements ICategoryService {
    @Autowired
    private ThreadDao threadMapper;
    @Autowired
    private IThreadService threadService;
    @Override
    public List<Category> getCategoriesWithThreadCount() {
        MPJLambdaWrapper<Category> wrapper = JoinWrappers.lambda(Category.class);
        wrapper.selectAll(Category.class)
                .leftJoin(Thread.class, Thread::getCategoryId, Category::getCategoryId)
                .groupBy(Category::getCategoryId);
        List<Category> categories = this.list(wrapper);
        for (Category category : categories) {
            MPJLambdaWrapper<Thread> threadWrapper = JoinWrappers.lambda(Thread.class);
            threadWrapper.eq(Thread::getCategoryId, category.getCategoryId());
            category.setThreadCount((int) threadService.count(threadWrapper));
        }
        return categories;
    }


}
