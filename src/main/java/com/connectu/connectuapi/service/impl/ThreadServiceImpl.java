package com.connectu.connectuapi.service.impl;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.ThreadDao;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.service.IThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThreadServiceImpl extends ServiceImpl<ThreadDao, Thread> implements IThreadService {
    @Autowired
    private ThreadDao threadDao;
    public void selectPage(Integer page, Integer amount){
        IPage pageImpl = new Page(page, amount);
        threadDao.selectPage(pageImpl, null);
    }

}
