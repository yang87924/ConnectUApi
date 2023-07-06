package com.connectu.connectuapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.controller.util.Code;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.dao.*;
import com.connectu.connectuapi.domain.*;
import com.connectu.connectuapi.domain.Thread;

import com.connectu.connectuapi.service.IFavoriteThreadService;
import com.connectu.connectuapi.service.IReplyService;
import com.connectu.connectuapi.service.IThreadService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import static com.connectu.connectuapi.service.utils.faker.getSystemTime;
@Transactional
@Service
public class ThreadServiceImpl extends MPJBaseServiceImpl<ThreadDao, Thread> implements IThreadService  {
    @Autowired
    private ThreadDao threadDao;
    @Autowired
    private ReplyDao replyDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private IFavoriteThreadService favoriteThreadService;
    @Autowired
    private HashtagDao hashtagDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ThreadHashtagDao threadHashtagDao;
    @Autowired
    private IReplyService replyService;

    public void handleHashtags(Thread thread, List<String> hashtags) {
        for (String hashtag : hashtags) {
            QueryWrapper<Hashtag> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", hashtag);
            Hashtag existingHashtag = hashtagDao.selectOne(queryWrapper);
            if (existingHashtag != null) {
                existingHashtag.setAmount(existingHashtag.getAmount() + 1);
                hashtagDao.updateById(existingHashtag);
            } else {
                Hashtag newHashtag = new Hashtag();
                newHashtag.setName(hashtag);
                newHashtag.setAmount(1);
                hashtagDao.insert(newHashtag);
                existingHashtag = newHashtag;
            }
            ThreadHashtag threadHashtag = new ThreadHashtag();
            threadHashtag.setThreadId(thread.getThreadId());
            threadHashtag.setHashtagId(existingHashtag.getHashtagId());
            threadHashtagDao.insert(threadHashtag);
        }
    }



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
//    @Override
//    public List<Thread> getFavoriteThreads(Integer userId) {
//        //查詢使用者收藏的文章ID
//        QueryWrapper<FavoriteThread> wrapper = new QueryWrapper<>();
//        wrapper.eq("userId", userId);
//        List<FavoriteThread> favoriteThreads = favoriteThreadService.list(wrapper);
//        List<Integer> threadIds = new ArrayList<>();
//        for (FavoriteThread favoriteThread : favoriteThreads) {
//            threadIds.add(favoriteThread.getThreadId());
//        }
//        if (threadIds.isEmpty()) {
//            return new ArrayList<>();
//        }
//        //查詢收藏的文章
//        QueryWrapper<Thread> threadWrapper = new QueryWrapper<>();
//        threadWrapper.in("threadId", threadIds);
//        List<Thread> threads = list(threadWrapper);
//        return threads;
//    }
    @Override
    public List<Thread> getFavoriteThreads(Integer userId) {
        List<Thread> threads = threadDao.getFavoriteThreads(userId);
        return threads;
    }


    @Override
    public List<Thread>getThreadById(Integer threadId){
        MPJLambdaWrapper<Thread> userWrapper = new MPJLambdaWrapper<>();
        userWrapper.innerJoin(Thread.class, Thread::getUserId, User::getUserId)
                .eq(Thread::getThreadId, threadId);
        return threadDao.selectList(userWrapper);
    }
    //熱門文章-----------------------------------------
    @Override
    public List<Thread> hotThread() {
        List<Thread> threads = threadDao.selectList(null);
        Map<Integer, Long> replyCountMap = getReplyCountMap(threads);
        for (Thread thread : threads) {
            Long replyCount = replyCountMap.getOrDefault(thread.getThreadId(), 0L);
            thread.setHotScore(thread.getLove() + thread.getFavoriteCount() + replyCount.intValue());
        }
        threads.sort(Comparator.comparing(Thread::getHotScore).reversed());
        List<Thread> hotThreads = threads.subList(0, Math.min(3, threads.size()));
        loadAdditionalData(hotThreads);
        return hotThreads;
    }
    private void loadAdditionalData(List<Thread> threads) {
        Set<Integer> categoryIds = new HashSet<>();
        Set<Integer> userIds = new HashSet<>();
        Set<Integer> threadIds = new HashSet<>();
        for (Thread thread : threads) {
            categoryIds.add(thread.getCategoryId());
            userIds.add(thread.getUserId());
            threadIds.add(thread.getThreadId());
        }
        Map<Integer, Category> categoryMap = getCategoryMap(categoryIds);
        Map<Integer, User> userMap = getUserMap(userIds);
        Map<Integer, List<Hashtag>> threadHashtagsMap = getThreadHashtagsMap(threadIds);
        for (Thread thread : threads) {
            Category category = categoryMap.get(thread.getCategoryId());
            if (category != null) {
                thread.setCategoryName(category.getCategoryName());
            }
            User user = userMap.get(thread.getUserId());
            if (user != null) {
                thread.setUser(user);
            }
            List<Hashtag> hashtags = threadHashtagsMap.get(thread.getThreadId());
            if (hashtags != null) {
                thread.setHashtags(hashtags);
            }
            thread.setReplyCount(replyService.getThreadReplyById(thread.getThreadId()).size());
        }
    }

    private Map<Integer, Category> getCategoryMap(Set<Integer> categoryIds) {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("categoryId", categoryIds);
        List<Category> categories = categoryDao.selectList(queryWrapper);
        Map<Integer, Category> categoryMap = new HashMap<>();
        for (Category category : categories) {
            categoryMap.put(category.getCategoryId(), category);
        }
        return categoryMap;
    }
    private Map<Integer, User> getUserMap(Set<Integer> userIds) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("userId", userIds);
        List<User> users = userDao.selectList(queryWrapper);
        Map<Integer, User> userMap = new HashMap<>();
        for (User user : users) {
            userMap.put(user.getUserId(), user);
        }
        return userMap;
    }
    private Map<Integer, List<Hashtag>> getThreadHashtagsMap(Set<Integer> threadIds) {
        QueryWrapper<ThreadHashtag> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("threadId", threadIds);
        List<ThreadHashtag> threadHashtags = threadHashtagDao.selectList(queryWrapper);
        Set<Integer> hashtagIds = new HashSet<>();
        for (ThreadHashtag threadHashtag : threadHashtags) {
            hashtagIds.add(threadHashtag.getHashtagId());
        }
        Map<Integer, Hashtag> hashtagMap = getHashtagMap(hashtagIds);
        Map<Integer, List<Hashtag>> threadHashtagsMap = new HashMap<>();
        for (ThreadHashtag threadHashtag : threadHashtags) {
            Integer threadId = threadHashtag.getThreadId();
            Hashtag hashtag = hashtagMap.get(threadHashtag.getHashtagId());
            if (hashtag != null) {
                threadHashtagsMap.computeIfAbsent(threadId, k -> new ArrayList<>()).add(hashtag);
            }
        }
        return threadHashtagsMap;
    }
    private Map<Integer, Hashtag> getHashtagMap(Set<Integer> hashtagIds) {
        QueryWrapper<Hashtag> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("hashtagId", hashtagIds);
        List<Hashtag> hashtags = hashtagDao.selectList(queryWrapper);
        Map<Integer, Hashtag> hashtagMap = new HashMap<>();
        for (Hashtag hashtag : hashtags) {
            hashtagMap.put(hashtag.getHashtagId(), hashtag);
        }
        return hashtagMap;
    }
    private Map<Integer, Long> getReplyCountMap(List<Thread> threads) {
        List<Integer> threadIds = threads.stream().map(Thread::getThreadId).collect(Collectors.toList());
        List<Reply> replies = replyDao.selectList(new QueryWrapper<Reply>().in("threadId", threadIds));
        return replies.stream().collect(Collectors.groupingBy(Reply::getThreadId, Collectors.counting()));
    }
    private Long getReplyCount(Integer threadId) {
        //ReplyDao replyDao = new ReplyDao();
        QueryWrapper<Reply> wrapper = new QueryWrapper<>();
        wrapper.eq("threadId", threadId);
        return replyDao.selectCount(wrapper);
    }
    //查詢使用者的所有文章--------------------------------------------------------------
    public List<Thread> getUserThread(int userId) {
        List<Thread> result = threadDao.selectList(new QueryWrapper<Thread>().eq("userId", userId));
        Set<Integer> categoryIds = result.stream().map(Thread::getCategoryId).collect(Collectors.toSet());
        if (categoryIds.isEmpty()) {
            return result;
        }
        Map<Integer, Category> categoryMap = categoryDao.selectBatchIds(categoryIds)
                .stream().collect(Collectors.toMap(Category::getCategoryId, c -> c));
        for (Thread thread : result) {
            Category category = categoryMap.get(thread.getCategoryId());
            if (category != null) {
                thread.setCategoryName(category.getCategoryName());
            }
        }
        return result;
    }
    public List<List<Thread>> getUserThreadForUser(List<Integer> userIds) {
        List<Thread> allThreads = threadDao.selectList(new QueryWrapper<Thread>().in("userId", userIds));
        Map<Integer, List<Thread>> userThreadsMap = allThreads.stream().collect(Collectors.groupingBy(Thread::getUserId));
        List<List<Thread>> userThreads = new ArrayList<>();
        for (Integer userId : userIds) {
            List<Thread> threads = userThreadsMap.getOrDefault(userId, new ArrayList<>());
            Set<Integer> categoryIds = threads.stream().map(Thread::getCategoryId).collect(Collectors.toSet());
            if (!categoryIds.isEmpty()) {
                Map<Integer, Category> categoryMap = categoryDao.selectBatchIds(categoryIds)
                        .stream().collect(Collectors.toMap(Category::getCategoryId, c -> c));
                for (Thread thread : threads) {
                    Category category = categoryMap.get(thread.getCategoryId());
                    if (category != null) {
                        thread.setCategoryName(category.getCategoryName());
                    }
                }
            }
            userThreads.add(threads);
        }
        return userThreads;
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
            // 加入 join user 資料表的資料
            User user = userDao.selectById(thread.getUserId());
            if (user != null) {
                thread.setUser(user);
            }
            thread.setReplyCount(replyService.getThreadReplyById(thread.getThreadId()).size());
        }
        return threads;
    }
    //查詢所有文章分頁查詢--------------------------------------------------------------
//    @Override
//    public IPage<Thread> listWithPagination(Page<Thread> page, Wrapper<Thread> queryWrapper) {
//        IPage<Thread> threadPage = super.page(page, queryWrapper);
//        List<Thread> threads = threadPage.getRecords();
//        for (Thread thread : threads) {
//            Category category = categoryDao.selectById(thread.getCategoryId());
//            if (category != null) {
//                thread.setCategoryName(category.getCategoryName());
//            }
//            User user = userDao.selectById(thread.getUserId());
//            if (user != null) {
//                thread.setUser(user);
//            }
//            thread.setReplyCount(replyService.getThreadReplyById(thread.getThreadId()).size());
//            // 查询ThreadHashtag表
//            List<ThreadHashtag> threadHashtags = threadHashtagDao.selectList(new QueryWrapper<ThreadHashtag>().eq("threadId", thread.getThreadId()));
//            List<Hashtag> hashtags = new ArrayList<>();
//            for (ThreadHashtag threadHashtag : threadHashtags) {
//                Hashtag hashtag = hashtagDao.selectById(threadHashtag.getHashtagId());
//                if (hashtag != null) {
//                    hashtags.add(hashtag);
//                }
//            }
//            thread.setHashtags(hashtags);
//        }
//        return threadPage;
//    }
    @Override
    public IPage<Thread> listWithPagination(Page<Thread> page, Wrapper<Thread> queryWrapper) {
        IPage<Thread> threadPage = super.page(page, queryWrapper);
        List<Thread> threads = threadPage.getRecords();
        // 批次查詢分類和使用者資訊
        List<Integer> categoryIds = threads.stream().map(Thread::getCategoryId).collect(Collectors.toList());
        List<Integer> userIds = threads.stream().map(Thread::getUserId).collect(Collectors.toList());
        Map<Integer, Category> categoryMap = categoryDao.selectBatchIds(categoryIds).stream().collect(Collectors.toMap(Category::getCategoryId, category -> category));
        Map<Integer, User> userMap = userDao.selectBatchIds(userIds).stream().collect(Collectors.toMap(User::getUserId, user -> user));
        for (Thread thread : threads) {
            Category category = categoryMap.get(thread.getCategoryId());
            if (category != null) {
                thread.setCategoryName(category.getCategoryName());
            }
            User user = userMap.get(thread.getUserId());
            if (user != null) {
                thread.setUser(user);
            }
            thread.setReplyCount(replyService.getThreadReplyById(thread.getThreadId()).size());
            // 批次查詢ThreadHashtag表
            List<ThreadHashtag> threadHashtags = threadHashtagDao.selectList(new QueryWrapper<ThreadHashtag>().eq("threadId", thread.getThreadId()));
            List<Integer> hashtagIds = threadHashtags.stream().map(ThreadHashtag::getHashtagId).collect(Collectors.toList());
            if (!hashtagIds.isEmpty()) {
                List<Hashtag> hashtags = hashtagDao.selectBatchIds(hashtagIds);
                thread.setHashtags(hashtags);
            }
        }
        return threadPage;
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
        MPJLambdaWrapper<Thread> threadWrapper = new MPJLambdaWrapper<>();
        threadWrapper
                .selectAll(Thread.class)
                .selectAll(Category.class)
                .leftJoin(Category.class, Category::getCategoryId, Thread::getCategoryId)
                .eq(Thread::getThreadId, threadId);
        Thread thread = threadDao.selectOne(threadWrapper);
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