package com.connectu.connectuapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.controller.util.Code;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.dao.CategoryDao;
import com.connectu.connectuapi.dao.ReplyDao;
import com.connectu.connectuapi.dao.ThreadDao;
import com.connectu.connectuapi.dao.UserDao;
import com.connectu.connectuapi.domain.*;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.exception.ThreadColumnIsNullException;

import com.connectu.connectuapi.exception.UserNotLoginException;
import com.connectu.connectuapi.service.IFavoriteThreadService;
import com.connectu.connectuapi.service.IReplyService;
import com.connectu.connectuapi.service.IThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
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
    @Autowired
    private IFavoriteThreadService favoriteThreadService;
    //假資料--------------------------------------------------------------
    @Override
    public void addFakeThread(int count) {
        for (int i = 0; i < count; i++) {
            Thread thread = ThreadServiceImpl.createFakeThread(count);
            threadDao.insert(thread);
        }
    }
    public static Thread createFakeThread(int count) {
        Thread thread = new Thread();
        // thread.setCategoryId((int) (Math.random() * 13) + 1);
        thread.setUserId((int) (Math.random() * 98) + 1);
        // thread.setTitle(generateFakeArticle(10));
        // thread.setContent(generateFakeArticle(100));
        // thread.setCreatedAt(getSystemTime());
        //  thread.setPicture("C:/Users/User/AppData/Local/Temp/tomcat-docbase.80.10138220504103279093/upload/95cf287d-00f7-4c44-aa49-a37eaa374270.png");
        return thread;
    }
    //新增收藏文章--------------------------------------------------------------
    @Override
    public boolean addFavoriteThread(Integer userId, Integer threadId) {
        //檢查使用者是否已經收藏過此文章
        QueryWrapper<FavoriteThread> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userId);
        wrapper.eq("threadId", threadId);
        FavoriteThread favoriteThread = favoriteThreadService.getOne(wrapper);
        if (favoriteThread != null) {
            //使用者已經收藏過此文章
            return true;
        }
        //新增收藏文章
        favoriteThread = new FavoriteThread();
        favoriteThread.setUserId(userId);
        favoriteThread.setThreadId(threadId);
        boolean flag = favoriteThreadService.save(favoriteThread);
        if (flag) {
            //收藏文章數量+1
            Thread thread = getById(threadId);
            thread.setFavoriteCount(thread.getFavoriteCount() + 1);
            updateById(thread);
        }
        return flag;
    }
    //新增文章--------------------------------------------------------------
    @Override
    public boolean save(Thread thread) {
        thread.setCreatedAt(getSystemTime());
        return super.save(thread);
    }
    //刪除文章--------------------------------------------------------------
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
    //切換使用者按讚--------------------------------------------------------------
    @Override
    public void toggleLove(Thread thread) {
        Integer love = thread.getLove();
        if (love % 2 == 0) {
            thread.setLove(love + 1);
        } else {
            thread.setLove(love - 1);
        }
    }
    //按讚--------------------------------------------------------------
    @Override
    public void love(Thread thread) {
        Integer love = thread.getLove() +1;
        thread.setLove(love);
    }
    //取消按讚--------------------------------------------------------------
    @Override
    public void cancelLove(Thread thread) {
        Integer love = thread.getLove() -1;
        thread.setLove(love);
    }
    //查詢使用者收藏的文章--------------------------------------------------------------
    @Override
    public List<Thread> getFavoriteThreads(Integer userId) {
        //查詢使用者收藏的文章ID
        QueryWrapper<FavoriteThread> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userId);
        List<FavoriteThread> favoriteThreads = favoriteThreadService.list(wrapper);
        List<Integer> threadIds = new ArrayList<>();
        for (FavoriteThread favoriteThread : favoriteThreads) {
            threadIds.add(favoriteThread.getThreadId());
        }
        if (threadIds.isEmpty()) {
            return new ArrayList<>();
        }
        //查詢收藏的文章
        QueryWrapper<Thread> threadWrapper = new QueryWrapper<>();
        threadWrapper.in("threadId", threadIds);
        List<Thread> threads = list(threadWrapper);
        return threads;
    }
    //熱門文章
    @Override
    public List<Thread> hotThread() {
        List<Thread> threads = threadDao.selectList(null);
        threads.forEach(thread -> {
            Long replyCount = getReplyCount(thread.getThreadId());
            //  Integer.parseInt(replyCount);
            thread.setHotScore(thread.getLove() + thread.getFavoriteCount() +replyCount.intValue());
            threadDao.updateById(thread);
        });
        threads.sort(Comparator.comparing(Thread::getHotScore).reversed());
        for (Thread thread : threads) {
            Category category = categoryDao.selectById(thread.getCategoryId());
            if (category != null) {
                thread.setCategoryName(category.getCategoryName());
            }
        }
        return threads;
    }
    private Long getReplyCount(Integer threadId) {
        //ReplyDao replyDao = new ReplyDao();
        QueryWrapper<Reply> wrapper = new QueryWrapper<>();
        wrapper.eq("threadId", threadId);
        return replyDao.selectCount(wrapper);
    }
    //查詢使用者的所有文章--------------------------------------------------------------
    public List<Thread> getUserThread(int userId) {
        LambdaQueryWrapper<Thread> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Thread::getUserId, userId);
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
    //查詢所有文章--------------------------------------------------------------
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

    //查詢主題的所有文章--------------------------------------------------------------
    public List<Thread> getCategoryThread(int categoryId) {
        LambdaQueryWrapper<Thread> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Thread::getCategoryId, categoryId);
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
    //查詢單筆論壇文章--------------------------------------------------------------
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
    //查詢最後一筆資料--------------------------------------------------------------
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
    //關鍵字搜尋--------------------------------------------------------------
    @Override
    public Result searchThreads(String keyword, String categoryName) {
        List<Thread> search = null;
        if (keyword != null && !keyword.isEmpty() && categoryName != null && !categoryName.isEmpty()) {
            search = searchThreadsByKeyword(keyword, categoryName);
        }
        Integer code = search != null && !search.isEmpty() ? Code.GET_OK : Code.GET_ERR;
        String msg = search != null && !search.isEmpty() ? "搜尋文章資料成功" : "搜尋文章資料失敗!請重新輸入關鍵字";
        return new Result(code, search, msg);
    }
    //關鍵字搜尋--------------------------------------------------------------
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