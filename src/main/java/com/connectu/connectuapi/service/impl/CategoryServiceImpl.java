package com.connectu.connectuapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.CategoryDao;
import com.connectu.connectuapi.dao.DyReplyDao;
import com.connectu.connectuapi.domain.Category;
import com.connectu.connectuapi.domain.DyReply;
import com.connectu.connectuapi.service.ICategoryService;
import com.connectu.connectuapi.service.IDyReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements ICategoryService {
    @Autowired
    private DyReplyDao dyReplyDao;


}
