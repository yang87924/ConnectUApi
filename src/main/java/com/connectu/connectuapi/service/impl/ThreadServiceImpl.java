package com.connectu.connectuapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.controller.BaseController;
import com.connectu.connectuapi.dao.CategoryDao;
import com.connectu.connectuapi.dao.ReplyDao;
import com.connectu.connectuapi.dao.ThreadDao;
import com.connectu.connectuapi.domain.Category;
import com.connectu.connectuapi.domain.Reply;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.domain.User;
import com.connectu.connectuapi.exception.PasswordNotMatchException;
import com.connectu.connectuapi.exception.UserNotFoundException;
import com.connectu.connectuapi.service.IThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.connectu.connectuapi.service.utils.faker.generateFakeArticle;
import static com.connectu.connectuapi.service.utils.faker.getSystemTime;
@Transactional
@Service
public class ThreadServiceImpl extends ServiceImpl<ThreadDao, Thread>  implements IThreadService  {
    @Autowired
    private ThreadDao threadDao;
    @Autowired
    private ReplyDao replyDao;
    @Autowired
    private CategoryDao categoryDao;

    @Override
    public void addFakeThread(int count) {
        for (int i = 0; i < count; i++) {
            Thread thread = ThreadServiceImpl.createFakeThread(count);
            threadDao.insert(thread);
        }
    }

    @Override
    public Integer getLastThreadById() {
        Integer lastThreadId = (Integer) threadDao.selectObjs(
                        new QueryWrapper<Thread>()
                                .select("threadId")
                                .orderByDesc("threadId")
                                .last("limit 1"))
                .get(0);
        return lastThreadId + 1;

    }
    @Override
    public Thread getThreadWithCategoryName(Integer threadId) {
        Thread thread = this.getById(threadId);
        if (thread != null) {
            Category category = categoryDao.selectById(thread.getCategoryId());
            if (category != null) {
                thread.setCategoryName(category.getCategoryName());
            }
        }
        return thread;
    }

    @Override
    public boolean saveThread(Integer categoryId,Integer userId,String title, String content,  String picture) {
        Thread thread=new Thread();
        thread.setTitle(title);
        thread.setContent(content);
        thread.setPicture(picture);
        thread.setCategoryId(categoryId);
        thread.setCreatedAt(getSystemTime());
        thread.setUserId(userId);
        return super.save(thread);
    }
//    @Override
//    public boolean save(Thread thread) {
//
//        thread.setCreatedAt(getSystemTime());
//        return super.save(thread);
//    }

    public static Thread createFakeThread(int count) {
        Thread thread = new Thread();
        thread.setCategoryId((int) (Math.random() * 13) + 1);
        thread.setUserId((int) (Math.random() * count) + 1);
        thread.setTitle(generateFakeArticle(10));
        thread.setContent(generateFakeArticle(100));
        thread.setCreatedAt(getSystemTime());
        thread.setPicture("C:/Users/User/AppData/Local/Temp/tomcat-docbase.80.10138220504103279093/upload/95cf287d-00f7-4c44-aa49-a37eaa374270.png");
        return thread;
    }
    public List<Thread> getUserThread(int id) {
        LambdaQueryWrapper<Thread> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Thread::getUserId, id);
        List<Thread> result = threadDao.selectList(lqw);
        // 為每個 thread 設置 categoryName
        for (Thread thread : result) {
            Category category = categoryDao.selectById(thread.getCategoryId());
            if (category != null) {
                thread.setCategoryName(category.getCategoryName());
            }
        }
        return result;
    }
    @Override
    public List<Thread> list(Wrapper<Thread> queryWrapper) {
        List<Thread> threads = super.list(queryWrapper);
        for (Thread thread : threads) {
            Category category = categoryDao.selectById(thread.getCategoryId());
            if (category != null) {
                thread.setCategoryName(category.getCategoryName());
            }
        }
        return threads;
    }

    @Override
    public boolean removeById(Serializable id) {
        LambdaQueryWrapper<Reply> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Reply::getThreadId, id);
        List<Reply> result = replyDao.selectList(lqw);

        for (Reply reply : result) {
            replyDao.deleteById(reply.getReplyId()) ;
        }
        return super.removeById(id);
    }
    @Override
    public List<Thread> searchThreadsByKeyword(String keyword, String categoryName) {
        LambdaQueryWrapper<Thread> lqw = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty() && categoryName != null && !categoryName.isEmpty()) {
            lqw.and(threadLqw -> threadLqw.like(Thread::getTitle, keyword)
                    .or()
                    .like(Thread::getContent, keyword));

            // 根据 categoryName 查询 categoryId
            LambdaQueryWrapper<Category> categoryLqw = new LambdaQueryWrapper<>();
            categoryLqw.like(Category::getCategoryName, categoryName);
            List<Category> categories = categoryDao.selectList(categoryLqw);
            if (!categories.isEmpty()) {
                List<Integer> categoryIds = categories.stream()
                        .map(Category::getCategoryId)
                        .collect(Collectors.toList());
                lqw.and(threadLqw -> threadLqw.in(Thread::getCategoryId, categoryIds));
            }

            List<Thread> result = threadDao.selectList(lqw);
            // 为每个 thread 设置 categoryName
            for (Thread thread : result) {
                Category category = categoryDao.selectById(thread.getCategoryId());
                if (category != null) {
                    thread.setCategoryName(category.getCategoryName());
                }
            }
            return result;
        } else {
            return new ArrayList<>();
        }
    }

}