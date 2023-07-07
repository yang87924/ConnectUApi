package com.connectu.connectuapi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.dao.*;
import com.connectu.connectuapi.domain.*;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.service.IThreadService;
import com.connectu.connectuapi.service.IUserService;
import com.connectu.connectuapi.service.impl.FriendshipServiceImpl;
import com.github.javafaker.Faker;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import io.swagger.models.auth.In;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Locale;

import static com.connectu.connectuapi.service.utils.faker.generateFakeArticle;
import static org.assertj.core.util.Lists.list;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ConnectUApiApplicationTests {
    @Autowired
    private IUserService userService;
    @Autowired
    private IThreadService threadService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private FriendshipDao friendshipDao;
    @Autowired
    private FriendshipServiceImpl friendshipService;
    @Autowired
    private ThreadDao threadDao;
    @Autowired
    private DyThreadDao dyThreadDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private UserThreadLoveDao userThreadLoveDao;

    //    @Autowired

    @Test
    void f1riendTest() {
        friendshipService.followingDyThread(2);
    }
    @Test
    void friendTest() {
        Integer followerId = 1;
        Integer followingId = 15;
        LambdaQueryWrapper<Friendship> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Friendship::getFollowerId, followerId)
                .eq(Friendship::getFollowingId, followingId);

        friendshipService.removeByIds(friendshipDao.selectList(lqw));

    }
    @Test
    void CategoryThread() {
        MPJLambdaWrapper<Thread> threadWrapper = new MPJLambdaWrapper<>();
        threadWrapper
                .selectAll(Thread.class)
                .innerJoin(Category.class, Category::getCategoryId, Category::getCategoryId)
                .eq(Category::getCategoryId, 2);
        List<Thread> threads = threadDao.selectList(threadWrapper);
        System.out.println(threads);
    }
    @Test
    void a(){
        MPJLambdaWrapper<Thread> wrapper = new MPJLambdaWrapper<>(Thread.class);
        wrapper
                .selectAll(Thread.class)
                .select(Category::getCategoryName)
                .selectAll(ThreadHashtag.class)
                .selectAll(Hashtag.class)
                .innerJoin(Category.class, Category::getCategoryId, Thread::getCategoryId)
                .innerJoin(ThreadHashtag.class,ThreadHashtag::getThreadId,Thread::getThreadId)
                .innerJoin(Hashtag.class,Hashtag::getHashtagId,ThreadHashtag::getHashtagId)
                .eq(Thread::getUserId, 2);
        list(wrapper).toString();
        System.out.println(list(wrapper));
    }
    @Test
    void c(){
        friendshipService.followingDyThread(4);
    }
    @Test
    void b(){
        MPJLambdaWrapper<Friendship> userWrapper = new MPJLambdaWrapper<>();
        userWrapper
                .selectAll(User.class)
                .selectAll(DyThread.class)
                .selectAll(dyThreadHashtag.class)
                .selectAll(Friendship.class)
                .selectAll(User.class)
                .selectAll(DyHashtag.class)
                .innerJoin(User.class,User::getUserId,Friendship::getFollowerId)
                .innerJoin(DyThread.class,DyThread::getUserId,Friendship::getFollowerId)
                .innerJoin(dyThreadHashtag.class,dyThreadHashtag::getDyThreadId,DyThread::getDyThreadId)
                .innerJoin(DyHashtag.class,DyHashtag::getDyHashtagId,dyThreadHashtag::getDyHashtagId)
                .eq(Friendship::getFollowingId, 2);
        List<Friendship> dythreads = friendshipDao.selectList(userWrapper);
        System.out.println(dythreads);
    }
    @Test
    void userTest() {
        Faker faker = new Faker(new Locale("zh_TW"));
        String loremText = generateRandomString(10, 50);
        System.out.println("Generated Lorem Text: " + loremText);
    }


    private static String generateRandomString(int minLength, int maxLength) {
        String characters = "在現代社會中，工作已成為我們生活的一部分。隨著科技的進步和全球化的影響，工作環境正在發生著巨大的變革。這些變化帶來了一些挑戰，同時也開啟了新的機會。\n" +
                "\n" +
                "一個主要的挑戰是適應快速變化的科技環境。隨著人工智能、大數據和自動化技術的普及，許多傳統的工作正在面臨被取代的風險。然而，這同時也創造了新的機會。我們可以利用科技的力量來改善工作效率，從而釋放出更多時間來專注於創造性和戰略性的任務。\n" +
                "\n" +
                "另一個挑戰是處理工作和生活之間的平衡。現代的工作節奏快速且繁忙，對我們的時間和精力提出了很大的要求。因此，我們需要學會管理時間，設定優先順序，並保持身心健康。這也是一個機會，讓我們探索彈性工作時間和遠程工作的模式，從而實現更好的工作與生活平衡。\n" +
                "\n" +
                "另外，團隊合作也是現代工作中的重要課題。越來越多的工作需要團隊間的合作和協調。這意味著我們需要培養良好的溝通和協作能力，學會在團隊中建立共享的目標和價值觀。同時，多元化的團隊結構也為創新和創造力提供了更多的機會，不同背景和觀點的人們能夠帶來更多的創意和解決問題的能力。\n" +
                "\n" +
                "最後，職業發展也是現代工作中的重要議題。工作市場變得競爭激烈，我們需要不斷學習和提升自己的技能，以應對變化中的需求。這也是一個機會，讓我們能夠";
        StringBuilder sb = new StringBuilder();
        int length = (int) (Math.random() * (maxLength - minLength + 1) + minLength);
        for (int i = 0; i < length; i++) {
            int randomIndex = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        return sb.toString();
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
    void loginTest() {
        userService.login("于思源", "kd2j88gaxqalh");
    }


}
