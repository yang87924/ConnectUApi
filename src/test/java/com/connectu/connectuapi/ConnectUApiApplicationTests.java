package com.connectu.connectuapi;

import com.baomidou.mybatisplus.core.toolkit.AES;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.dao.UserDao;
import com.connectu.connectuapi.service.IThreadService;
import com.connectu.connectuapi.service.IUserService;
import org.jasypt.encryption.StringEncryptor;
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
    private StringEncryptor stringEncryptor;
    @Test
    void userTest() {
        userService.list(null);
    }

    @Test
    void threadTest() {
//        threadService.(5);
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
    @Test
    void encryptPwd(){

//        String secret="connectU";
//        String url="jdbc:mysql://mydbinstance2.cmcjn8qqcxqk.ap-northeast-1.rds.amazonaws.com:3306/connectU?serverTimezone=UTC";
//        System.out.println("ENC("+stringEncryptor.encrypt(url)+")");
//
//        String userName="admin";
//        System.out.println("ENC("+stringEncryptor.encrypt(userName)+")");
//
//        String password="GBb4ddyE1mfrBFGQA5sc";
//        System.out.println("ENC("+stringEncryptor.encrypt(password)+")");
        //解密方法
        System.out.println(stringEncryptor.decrypt("oeuu4ruGs8bI1MNZuf7eQE4YvTYpMUufHpBKTJreR8qx0Hh5idsNi7qAZbGFXPyd"));

    }

}
