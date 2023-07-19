package com.connectu.connectuapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.connectu.connectuapi.controller.util.Code;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.dao.*;
import com.connectu.connectuapi.domain.*;
import com.connectu.connectuapi.domain.Thread;

import com.connectu.connectuapi.service.*;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
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
    private ThreadHashtagDao threadHashtagDao;
    @Autowired
    private IReplyService replyService;
    @Autowired
    private IThreadHashtagService iThreadHashtagService;
    @Autowired
    private IHashtagService iHashtagService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserThreadLoveDao userThreadLoveDao;
    @Autowired
    private FavoriteThreadDao favoriteThreadDao;
    //查詢使用者的所有文章--------------------------------------------------------------
    @Override
    public List<Thread> getUserThread(Integer userId) {
        // Fetch all threads by the user
        QueryWrapper<Thread> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", userId);
        queryWrapper.orderByDesc("threadId");
        List<Thread> threads = threadDao.selectList(queryWrapper);

        // For each thread, fetch and set the related data
        for (Thread thread : threads) {
            // Fetch and set the user
            User user = userDao.selectById(thread.getUserId());
            thread.setUser(user);

            // Fetch and set the replies
            QueryWrapper<Reply> replyQueryWrapper = new QueryWrapper<>();
            replyQueryWrapper.eq("threadId", thread.getThreadId());
            List<Reply> replies = replyDao.selectList(replyQueryWrapper);
            thread.setReplyCount(replies.size()); // Set the reply count

            // Fetch and set the hashtags
            QueryWrapper<ThreadHashtag> threadHashtagQueryWrapper = new QueryWrapper<>();
            threadHashtagQueryWrapper.eq("threadId", thread.getThreadId());
            List<ThreadHashtag> threadHashtags = threadHashtagDao.selectList(threadHashtagQueryWrapper);
            List<Hashtag> hashtags = new ArrayList<>();
            for (ThreadHashtag threadHashtag : threadHashtags) {
                Hashtag hashtag = hashtagDao.selectById(threadHashtag.getHashtagId());
                hashtags.add(hashtag);
            }
            thread.setHashtags(hashtags);
        }


        return threads;
    }


    public void saveThreadHashtags(Thread thread) {
        List<Hashtag> hashtags = thread.getHashtags();
        if (hashtags != null && !hashtags.isEmpty()) {
            for (Hashtag hashtag : hashtags) {
                ThreadHashtag threadHashtag = new ThreadHashtag();
                threadHashtag.setThreadId(thread.getThreadId());
                threadHashtag.setHashtagId(hashtag.getHashtagId());
                threadHashtagDao.insert(threadHashtag);
            }
        }
    }
    @Override
    public Thread getLastThreadWithDetails() {
        Thread lastThread = threadDao.selectOne(
                new QueryWrapper<Thread>()
                        .orderByDesc("threadId")
                        .last("limit 1")
        );

        if (lastThread != null) {
            // 获取对应的Hashtag数据
            List<Hashtag> hashtags = hashtagDao.selectList(
                    new QueryWrapper<Hashtag>()
                            .inSql("hashtagId", "SELECT hashtagId FROM threadHashtag WHERE threadId = " + lastThread.getThreadId())
            );
            System.out.println(hashtags);
            lastThread.setHashtags(hashtags);

            // 获取对应的categoryName
            Category category = categoryDao.selectById(lastThread.getCategoryId());
            if (category != null) {
                lastThread.setCategoryName(category.getCategoryName());
            }
            // 获取对应的User数据
            User user = userDao.selectById(lastThread.getUserId());
            if (user != null) {
                lastThread.setUser(user);
            }
        }

        return lastThread;
    }
    @Override
    public boolean deleteByThreadId(Integer userId) {
        // 获取要删除的Thread记录
        QueryWrapper<Thread> threadWrapper = new QueryWrapper<>();
        threadWrapper.eq("userId", userId);
        List<Thread> threads = threadDao.selectList(threadWrapper);
        if (threads.isEmpty()) {
            return false;
        }

        // 获取要删除的Thread记录的threadId列表
        List<Integer> threadIds = threads.stream().map(Thread::getThreadId).collect(Collectors.toList());
        // Delete related records in UserThreadLove table
        QueryWrapper<UserThreadLove> userThreadLoveWrapper = new QueryWrapper<>();
        userThreadLoveWrapper.eq("threadId", threadIds);
        userThreadLoveDao.delete(userThreadLoveWrapper);
        // 删除FavoriteThread表中与Thread相关联的记录
        QueryWrapper<FavoriteThread> favoriteThreadWrapper = new QueryWrapper<>();
        favoriteThreadWrapper.eq("threadId", threadIds);
        favoriteThreadDao.delete(favoriteThreadWrapper);
        // 删除ThreadHashtag表中与Thread相关联的记录
        QueryWrapper<ThreadHashtag> threadHashtagWrapper = new QueryWrapper<>();
        threadHashtagWrapper.eq("threadId", threadIds);
        threadHashtagDao.delete(threadHashtagWrapper);





        // 删除Reply表中与Thread相关联的记录
        QueryWrapper<Reply> replyWrapper = new QueryWrapper<>();
        replyWrapper.in("threadId", threadIds);
        replyDao.delete(replyWrapper);

        // 删除Thread表中指定userId的记录
        threadDao.delete(threadWrapper);

        return true;
    }

    @Override
    public Page<Thread> getThreadByPage(Page<Thread> page) {
        QueryWrapper<Thread> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("threadId");
        Page<Thread> threadPage = this.page(page, wrapper);
        if (threadPage.getRecords().isEmpty()) {
            return null;
        }
        for (Thread thread : threadPage.getRecords()) {
            // Fill user information
            User user = userDao.selectById(thread.getUserId());
            thread.setUser(user);
            // Fill hashtag information
            QueryWrapper<ThreadHashtag> threadHashtagWrapper = new QueryWrapper<>();
            threadHashtagWrapper.eq("threadId", thread.getThreadId());
            List<ThreadHashtag> threadHashtags = threadHashtagDao.selectList(threadHashtagWrapper);
            List<Hashtag> hashtags = new ArrayList<>();
            for (ThreadHashtag threadHashtag : threadHashtags) {
                Hashtag hashtag = hashtagDao.selectById(threadHashtag.getHashtagId());
                hashtags.add(hashtag);
            }
            thread.setHashtags(hashtags);
            // 填充 categoryName 信息
            Category category = categoryDao.selectById(thread.getCategoryId());
            thread.setCategoryName(category.getCategoryName());
        }
        return threadPage;
    }

    @Override
    public void handleHashtags(String threadHashtags, Thread thread) {
        List<Hashtag> hashtags = new ArrayList<>();
        String[] tags = threadHashtags.split("#");
        for (String tag : tags) {
            if (!tag.isEmpty()) {
                Hashtag existingHashtag = hashtagDao.selectOne(new QueryWrapper<Hashtag>().eq("name", tag));
                if (existingHashtag != null) {
                    existingHashtag.setAmount(existingHashtag.getAmount() + 1);
                    hashtagDao.updateById(existingHashtag);
                    hashtags.add(existingHashtag);
                } else {
                    Hashtag newHashtag = new Hashtag();
                    newHashtag.setName(tag);
                    newHashtag.setAmount(1);
                    hashtagDao.insert(newHashtag);
                    hashtags.add(newHashtag);
                }
            }
        }
        thread.setHashtags(hashtags);
    }





    @Override
    public IPage<Thread> listWithPagination(Page<Thread> page, Wrapper<Thread> queryWrapper) {
        IPage<Thread> threadPage = threadDao.selectPage(page, queryWrapper);
        List<Thread> threads = threadPage.getRecords();
        List<Integer> userIds = threads.stream()
                .map(Thread::getUserId)
                .collect(Collectors.toList());
        Map<Integer, User> userMap = userDao.selectBatchIds(userIds).stream()
                .collect(Collectors.toMap(User::getUserId, user -> user));
        for (Thread thread : threads) {
            User user = userMap.get(thread.getUserId());
            if (user != null) {
                thread.setUser(user);
            }
            thread.setReplyCount(replyService.getThreadReplyById(thread.getThreadId()).size());
            // 批次查詢ThreadHashtag表
            List<ThreadHashtag> ThreadHashtags = threadHashtagDao.selectList(new QueryWrapper<ThreadHashtag>().eq("threadId", thread.getThreadId()));
            List<Integer> hashtagIds = ThreadHashtags.stream().map(ThreadHashtag::getHashtagId).collect(Collectors.toList());
            if (!hashtagIds.isEmpty()) {
                List<Hashtag> hashtags = hashtagDao.selectBatchIds(hashtagIds);
                thread.setHashtags(hashtags);
            }
        }
        threadPage.setRecords(threads);
        return threadPage;
    }




//    public void handleHashtags(Thread thread, List<String> hashtags) {
//        for (String hashtag : hashtags) {
//            QueryWrapper<Hashtag> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("name", hashtag);
//            Hashtag existingHashtag = hashtagDao.selectOne(queryWrapper);
//            if (existingHashtag != null) {
//                existingHashtag.setAmount(existingHashtag.getAmount() + 1);
//                hashtagDao.updateById(existingHashtag);
//            } else {
//                Hashtag newHashtag = new Hashtag();
//                newHashtag.setName(hashtag);
//                newHashtag.setAmount(1);
//                hashtagDao.insert(newHashtag);
//                existingHashtag = newHashtag;
//            }
//            ThreadHashtag threadHashtag = new ThreadHashtag();
//            threadHashtag.setThreadId(thread.getThreadId());
//            threadHashtag.setHashtagId(existingHashtag.getHashtagId());
//            threadHashtagDao.insert(threadHashtag);
//        }
//    }



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
        // 查詢使用者收藏的文章ID
        List<Integer> threadIds = favoriteThreadService.lambdaQuery()
                .eq(FavoriteThread::getUserId, userId)
                .list()
                .stream()
                .map(FavoriteThread::getThreadId)
                .collect(Collectors.toList());
        if (threadIds.isEmpty()) {
            return new ArrayList<>();
        }
        // 查詢收藏的文章
        List<Thread> threads = lambdaQuery()
                .in(Thread::getThreadId, threadIds)
                .list();
        // 查詢所有文章的Hashtag的資料
        Map<Integer, List<Hashtag>> threadHashtags = getHashtagsByThreadIds(threadIds);
        for (Thread thread : threads) {
            thread.setHashtags(threadHashtags.get(thread.getThreadId()));
            thread.setUser(userDao.selectById(thread.getUserId())); // 添加查詢使用者資料的邏輯
        }
        return threads;
    }

    private Map<Integer, List<Hashtag>> getHashtagsByThreadIds(List<Integer> threadIds) {
        // 查詢所有文章的Hashtag ID
        List<ThreadHashtag> threadHashtags = iThreadHashtagService.lambdaQuery()
                .in(ThreadHashtag::getThreadId, threadIds)
                .list();
        List<Integer> hashtagIds = threadHashtags.stream()
                .map(ThreadHashtag::getHashtagId)
                .collect(Collectors.toList());
        if (hashtagIds.isEmpty()) {
            return new HashMap<>();
        }
        // 查詢所有Hashtag
        List<Hashtag> hashtags = iHashtagService.lambdaQuery()
                .in(Hashtag::getHashtagId, hashtagIds)
                .list();
        // 將Hashtag按照文章ID分組
        Map<Integer, List<Hashtag>> threadHashtagsMap = new HashMap<>();
        for (ThreadHashtag threadHashtag : threadHashtags) {
            if (!threadHashtagsMap.containsKey(threadHashtag.getThreadId())) {
                threadHashtagsMap.put(threadHashtag.getThreadId(), new ArrayList<>());
            }
            for (Hashtag hashtag : hashtags) {
                if (hashtag.getHashtagId().equals(threadHashtag.getHashtagId())) {
                    threadHashtagsMap.get(threadHashtag.getThreadId()).add(hashtag);
                }
            }
        }
        return threadHashtagsMap;
    }


//    @Override
//    public List<Thread> getFavoriteThreads(Integer userId) {
//        List<Thread> threads = threadDao.getFavoriteThreads(userId);
//        User user = userDao.selectById(thread.getUserId());
//        thread.setUser(user);
//
//        // 获取并设置 Hashtag 信息
//        LambdaQueryWrapper<ThreadHashtag> threadHashtagQueryWrapper = new LambdaQueryWrapper<>();
//        threadHashtagQueryWrapper.eq(ThreadHashtag::getThreadId, thread.getThreadId());
//        List<ThreadHashtag> threadHashtags = threadHashtagDao.selectList(threadHashtagQueryWrapper);
//        List<Hashtag> hashtags = new ArrayList<>();
//        for (ThreadHashtag threadHashtag : threadHashtags) {
//            Hashtag hashtag = hashtagDao.selectById(threadHashtag.getHashtagId());
//            hashtags.add(hashtag);
//        }
//        thread.setHashtags(hashtags);
//        return threads;
//    }




    @Override
    public List<Thread>getThreadById(Integer threadId){
        MPJLambdaWrapper<Thread> userWrapper = new MPJLambdaWrapper<>();
        userWrapper.innerJoin(Thread.class, Thread::getUserId, User::getUserId)
                .eq(Thread::getThreadId, threadId);
        return threadDao.selectList(userWrapper);
    }

    @Override
    public void processHashtags(String hashtags, Thread thread) {
        if (hashtags != null && !hashtags.isEmpty()) {
            List<String> hashtagList = Arrays.asList(hashtags.split("#"));
            for (String tag : hashtagList) {
                if (!tag.trim().isEmpty()) {
                    // 创建Hashtag对象并设置属性
                    Hashtag hashtag = new Hashtag();
                    hashtag.setName(tag);
                    hashtag.setAmount(1); // 初始化为1

                    // 判断是否已存在该Hashtag
                    QueryWrapper<Hashtag> wrapper = new QueryWrapper<>();
                    wrapper.eq("name", tag);
                    Hashtag existingHashtag = hashtagDao.selectOne(wrapper);

                    if (existingHashtag != null) {
                        // 已存在，更新amount
                        existingHashtag.setAmount(existingHashtag.getAmount() + 1);
                        hashtagDao.updateById(existingHashtag);
                    } else {
                        // 不存在，新增Hashtag
                        hashtagDao.insert(hashtag);
                    }

                    // 创建ThreadHashtag对象并设置属性
                    ThreadHashtag threadHashtag = new ThreadHashtag();
                    threadHashtag.setThreadId(thread.getThreadId());
                    threadHashtag.setHashtagId(hashtag.getHashtagId());
                    threadHashtagDao.insert(threadHashtag);
                }
            }
        }
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
//    @Override
//    public List<Thread> list(Wrapper<Thread> queryWrapper) {
//        List<Thread> threads = super.list(queryWrapper);
//        for (Thread thread : threads) {
//            Category category = categoryDao.selectById(thread.getCategoryId());
//            if (category != null) {
//                thread.setCategoryName(category.getCategoryName());
//            }
//            // 加入 join user 資料表的資料
//            User user = userDao.selectById(thread.getUserId());
//            if (user != null) {
//                thread.setUser(user);
//            }
//            thread.setReplyCount(replyService.getThreadReplyById(thread.getThreadId()).size());
//        }
//        return threads;
//    }
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
//    @Override
//    public IPage<Thread> listWithPagination(Page<Thread> page, Wrapper<Thread> queryWrapper) {
//        IPage<Thread> threadPage = super.page(page, queryWrapper);
//        List<Thread> threads = threadPage.getRecords();
//        // 處理資料
//        // Collections.reverse(dyThreads);
//        // 批次查詢分類和使用者資訊
//        List<Integer> userIds = threads.stream().map(Thread::getUserId).collect(Collectors.toList());
//        Map<Integer, User> userMap = userDao.selectBatchIds(userIds).stream().collect(Collectors.toMap(User::getUserId, user -> user));
//        for (Thread thread : threads) {
//            User user = userMap.get(thread.getUserId());
//            if (user != null) {
//                thread.setUser(user);
//            }
//            thread.setReplyCount(replyService.getThreadReplyById(thread.getThreadId()).size());
//            // 批次查詢ThreadHashtag表
//            List<ThreadHashtag> ThreadHashtags = threadHashtagDao.selectList(new QueryWrapper<ThreadHashtag>().eq("threadId", thread.getThreadId()));
//            List<Integer> hashtagIds = ThreadHashtags.stream().map(ThreadHashtag::getHashtagId).collect(Collectors.toList());
//            if (!hashtagIds.isEmpty()) {
//                List<Hashtag> hashtags = hashtagDao.selectBatchIds(hashtagIds);
//                thread.setHashtags(hashtags);
//            }
//        }
//        threadPage.setRecords(threads);
//        return threadPage;
//    }

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
        // 获取并设置 User 信息
        User user = userDao.selectById(thread.getUserId());
        thread.setUser(user);

        // 获取并设置 Hashtag 信息
        LambdaQueryWrapper<ThreadHashtag> threadHashtagQueryWrapper = new LambdaQueryWrapper<>();
        threadHashtagQueryWrapper.eq(ThreadHashtag::getThreadId, thread.getThreadId());
        List<ThreadHashtag> threadHashtags = threadHashtagDao.selectList(threadHashtagQueryWrapper);
        List<Hashtag> hashtags = new ArrayList<>();
        for (ThreadHashtag threadHashtag : threadHashtags) {
            Hashtag hashtag = hashtagDao.selectById(threadHashtag.getHashtagId());
            hashtags.add(hashtag);
        }
        thread.setHashtags(hashtags);

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
    public Result searchThreads(String keyword) {
        List<Thread> search = null;
        if (keyword != null && !keyword.isEmpty()) {
            search = searchThreadsByKeyword(keyword);
        }
        Integer code = search != null && !search.isEmpty() ? Code.GET_OK : Code.GET_ERR;
        String msg = search != null && !search.isEmpty() ? "搜尋文章資料成功" : "搜尋文章資料失敗!請重新輸入關鍵字";
        return new Result(code, search, msg);
    }

    @Override
    public List<Thread> searchThreadsByKeyword(String keyword) {
        LambdaQueryWrapper<Thread> lqw = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isEmpty()) {
            lqw.and(threadLqw -> threadLqw
                    .like(Thread::getTitle, keyword)
                    .or()
                    .like(Thread::getContent, keyword));

            List<Thread> result = threadDao.selectList(lqw);
            // Fetch User info
            List<Integer> userIds = result.stream().map(Thread::getUserId).collect(Collectors.toList());
            Map<Integer, User> userMap = userDao.selectBatchIds(userIds).stream().collect(Collectors.toMap(User::getUserId, user -> user));

            // Initiate an empty Map to hold DyHashtag
            Map<Integer, Hashtag> hashtagMap = new HashMap<>();

            for (Thread thread : result) {
                User user = userMap.get(thread.getUserId());
                if (user != null) {
                    thread.setUser(user);
                }
                thread.setReplyCount(replyService.getThreadReplyById(thread.getThreadId()).size());

                // Fetch dyThreadHashtag info
                List<ThreadHashtag> dyThreadHashtags = threadHashtagDao.selectList(new QueryWrapper<ThreadHashtag>().eq("threadId", thread.getThreadId()));
                List<Integer> hashtagIds = dyThreadHashtags.stream().map(ThreadHashtag::getHashtagId).collect(Collectors.toList());

                // Fetch DyHashtag info if not fetched already
                if (!hashtagIds.isEmpty()) {
                    // Check and fetch only those DyHashtag not already fetched
                    List<Integer> notFetchedHashtagIds = hashtagIds.stream().filter(ids -> !hashtagMap.containsKey(ids)).collect(Collectors.toList());
                    if(!notFetchedHashtagIds.isEmpty()){
                        List<Hashtag> hashtags = hashtagDao.selectBatchIds(notFetchedHashtagIds);
                        hashtags.forEach(hashtag -> hashtagMap.put(hashtag.getHashtagId(), hashtag));
                    }

                    // Add DyHashtags to DyThread
                    List<Hashtag> hashtags = hashtagIds.stream().map(hashtagMap::get).collect(Collectors.toList());
                    thread.setHashtags(hashtags);
                }
            }
            return result;
        } else {
            return new ArrayList<>();
        }
    }
}