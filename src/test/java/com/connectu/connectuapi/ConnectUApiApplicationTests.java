package com.connectu.connectuapi;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.connectu.connectuapi.dao.UserDao;
import com.connectu.connectuapi.service.IThreadService;
import com.connectu.connectuapi.service.IUserService;
import com.connectu.connectuapi.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConnectUApiApplicationTests {
    @Autowired
    private IUserService userService;
    @Autowired
    private IThreadService threadService;
//    private UserDao userDao;
    @Test
    void userTest() {
        userService.list(null);
    }

    @Test
    void threadTest() {
        threadService.list(null);
    }

    @Test
    void threadPageTest() {
        threadService.selectPage(1,5);
    }

}
