package com.connectu.connectuapi.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.dao.DyThreadDao;
import com.connectu.connectuapi.dao.impl.DyThread;
import com.connectu.connectuapi.service.IDyThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DyThreadServiceImpl extends ServiceImpl<DyThreadDao, DyThread> implements IDyThreadService {
    @Autowired
    private DyThreadDao dythreadDao;

}
