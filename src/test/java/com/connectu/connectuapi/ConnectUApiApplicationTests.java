package com.connectu.connectuapi;

import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.dao.UserDao;
import com.connectu.connectuapi.dao.UserThreadLoveDao;
import com.connectu.connectuapi.service.IThreadService;
import com.connectu.connectuapi.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ConnectUApiApplicationTests {
    @Autowired
    private IUserService userService;
    @Autowired
    private IThreadService threadService;
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserThreadLoveDao userThreadLoveDao;
//    @Autowired
//    private StringEncryptor stringEncryptor;
    @Test
    void userTest() {
        userService.list(null);
    }

    @Test
    void threadTest() {
        userThreadLoveDao.selectList(null);
    }

    @Test
    void threadPageTest() {
        //threadService.selectPage(1,5);
    }
    @Test
    void Test() {
        System.out.println(new Result(1111, null, "yoyoyo"));
    }
    @Test
    void loginTest(){
        userService.login("于思源", "kd2j88gaxqalh");
    }


}
