package com.connectu.connectuapi.service.impl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.connectu.connectuapi.dao.*;
import com.connectu.connectuapi.domain.*;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.service.IDyReplyService;
import com.connectu.connectuapi.service.IDyThreadService;
import com.connectu.connectuapi.service.IFavoriteDyThreadService;
import com.connectu.connectuapi.service.IReplyService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.connectu.connectuapi.service.utils.faker.generateFakeArticle;
import static com.connectu.connectuapi.service.utils.faker.getSystemTime;

@Service
public class DyThreadServiceImpl extends MPJBaseServiceImpl<DyThreadDao, DyThread> implements IDyThreadService {
    @Autowired
    private DyThreadDao dythreadDao;
    @Autowired
    private DyReplyDao dyReplyDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private IFavoriteDyThreadService favoriteDyThreadService;
    @Autowired
    private DyThreadHashtagDao dyThreadHashtagDao;
    @Autowired
    private DyHashtagDao dyHashtagDao;
    @Autowired
    private IDyReplyService dyReplyService;
    public void handleHashtags(DyThread dyThread, List<String> dyHashtags) {
        for (String dyHashtag : dyHashtags) {
            QueryWrapper<DyHashtag> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name", dyHashtag);
            DyHashtag existingHashtag = dyHashtagDao.selectOne(queryWrapper);
            if (existingHashtag != null) {
                existingHashtag.setAmount(existingHashtag.getAmount() + 1);
                dyHashtagDao.updateById(existingHashtag);
            } else {
                DyHashtag newDyHashtag = new DyHashtag();
                newDyHashtag.setName(dyHashtag);
                newDyHashtag.setAmount(1);
                dyHashtagDao.insert(newDyHashtag);
                existingHashtag = newDyHashtag;
            }
            dyThreadHashtag dyThreadHashtag = new dyThreadHashtag();
            dyThreadHashtag.setDyThreadHashtagId(dyThread.getDyThreadId());
            dyThreadHashtag.setDyHashtagId(existingHashtag.getDyHashtagId());
            dyThreadHashtagDao.insert(dyThreadHashtag);
        }
    }
    @Override
    public IPage<DyThread> listWithPagination(Page<DyThread> page, Wrapper<DyThread> queryWrapper) {
        IPage<DyThread> threadPage = super.page(page, queryWrapper);
        List<DyThread> dyThreads = threadPage.getRecords();
        // 批次查詢分類和使用者資訊

        List<Integer> userIds = dyThreads.stream().map(DyThread::getUserId).collect(Collectors.toList());
        Map<Integer, User> userMap = userDao.selectBatchIds(userIds).stream().collect(Collectors.toMap(User::getUserId, user -> user));
        for (DyThread dyThread : dyThreads) {

            User user = userMap.get(dyThread.getUserId());
            if (user != null) {
                dyThread.setUser(user);
            }
            dyThread.setReplyCount(dyReplyService.getDyThreadReplyById(dyThread.getDyThreadId()).size());
            // 批次查詢ThreadHashtag表
            List<dyThreadHashtag> dyThreadHashtags = dyThreadHashtagDao.selectList(new QueryWrapper<dyThreadHashtag>().eq("dyThreadId", dyThread.getDyThreadId()));
            List<Integer> hashtagIds = dyThreadHashtags.stream().map(dyThreadHashtag::getDyHashtagId).collect(Collectors.toList());
            if (!hashtagIds.isEmpty()) {
                List<DyHashtag> hashtags = dyHashtagDao.selectBatchIds(hashtagIds);
                dyThread.setHashtags(hashtags);
            }
        }
        return threadPage;
    }
    //假資料
    @Override
    public void addFakeDyThread(int count) {
        for (int i = 0; i < count; i++) {
            DyThread thread = DyThreadServiceImpl.createFakeDyThread(count);
            dythreadDao.insert(thread);
        }
    }
    public static DyThread createFakeDyThread(int count) {
        DyThread dythread = new DyThread();
        dythread.setUserId((int) (Math.random() * count) + 1);
       // dythread.setTitle(generateFakeArticle(10));
        dythread.setContent(generateFakeArticle(100));
        dythread.setCreatedAt(getSystemTime());
        return dythread;
    }
    //新增收藏文章--------------------------------------------------------------
    @Override
    public boolean addFavoriteDyThread(Integer userId, Integer dyThreadId) {
        //檢查使用者是否已經收藏過此文章
        QueryWrapper<FavoriteDyThread> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userId);
        wrapper.eq("dyThreadId", dyThreadId);
        FavoriteDyThread favoriteDyThread = favoriteDyThreadService.getOne(wrapper);
        if (favoriteDyThread != null) {
            //使用者已經收藏過此文章
            return true;
        }
        //新增收藏文章
        favoriteDyThread = new FavoriteDyThread();
        favoriteDyThread.setUserId(userId);
        favoriteDyThread.setDyThreadId(dyThreadId);
        boolean flag = favoriteDyThreadService.save(favoriteDyThread);
        if (flag) {
            //收藏文章數量+1
            DyThread dyThread = getById(dyThreadId);
            dyThread.setFavoriteCount(dyThread.getFavoriteCount() + 1);
            updateById(dyThread);
        }
        return flag;
    }
    //切換使用者按讚--------------------------------------------------------------
    @Override
    public void toggleLove(DyThread dyThread) {
        Integer love = dyThread.getLove();
        if (love % 2 == 0) {
            dyThread.setLove(love + 1);
        } else {
            dyThread.setLove(love - 1);
        }
    }
    @Override
    public boolean save(DyThread dyThread) {
        dyThread.setCreatedAt(getSystemTime());
        return super.save(dyThread);
    }
    public List<DyThread> getUserDyThreadById(int id) {
        LambdaQueryWrapper<DyThread> lqw = new LambdaQueryWrapper<>();
        lqw.eq(DyThread::getUserId, id);
        List<DyThread> result = dythreadDao.selectList(lqw);
        return result;
    }
    //熱門文章
    @Override
    public List<DyThread> hotDyhread() {
        List<DyThread> dyThreads =dythreadDao.selectList(null);
        Map<Integer, Long> replyCountMap = getReplyCountMap(dyThreads);
        for (DyThread dyThread : dyThreads) {
            Long replyCount = replyCountMap.getOrDefault(dyThread.getDyThreadId(), 0L);
            dyThread.setHotScore(dyThread.getLove() + dyThread.getFavoriteCount() + replyCount.intValue());
        }
        dyThreads.sort(Comparator.comparing(DyThread::getHotScore).reversed());
        List<DyThread> hotThreads = dyThreads.subList(0, Math.min(3, dyThreads.size()));
        loadAdditionalData(hotThreads);
        return hotThreads;
    }
    private void loadAdditionalData(List<DyThread> dyThreads) {
        //Set<Integer> categoryIds = new HashSet<>();
        Set<Integer> userIds = new HashSet<>();
        Set<Integer> dyThreadIds = new HashSet<>();
        for (DyThread dyThread : dyThreads) {
            //categoryIds.add(thread.getCategoryId());
            userIds.add(dyThread.getUserId());
            dyThreadIds.add(dyThread.getDyThreadId());
        }
        //Map<Integer, Category> categoryMap = getCategoryMap(categoryIds);
        Map<Integer, User> userMap = getUserMap(userIds);
        Map<Integer, List<DyHashtag>> dyThreadHashtagsMap = getThreadHashtagsMap(dyThreadIds);
        for (DyThread dyThread : dyThreads) {

            User user = userMap.get(dyThread.getUserId());
            if (user != null) {
                dyThread.setUser(user);
            }
            List<DyHashtag> hashtags = dyThreadHashtagsMap.get(dyThread.getDyThreadId());
            if (hashtags != null) {
                dyThread.setHashtags(hashtags);
            }
            dyThread.setReplyCount(dyReplyService.getDyThreadReplyById(dyThread.getDyThreadId()).size());
        }
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
    private Map<Integer, List<DyHashtag>> getThreadHashtagsMap(Set<Integer> dyThreadIds) {
        QueryWrapper<dyThreadHashtag> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("dyThreadId", dyThreadIds);
        List<dyThreadHashtag> dyThreadHashtags = dyThreadHashtagDao.selectList(queryWrapper);
        Set<Integer> dyHashtagIds = new HashSet<>();
        for (dyThreadHashtag dyThreadHashtag : dyThreadHashtags) {
            dyHashtagIds.add(dyThreadHashtag.getDyHashtagId());
        }
        Map<Integer, DyHashtag> DyHashtagMap = getDyHashtagMap(dyHashtagIds);
        Map<Integer, List<DyHashtag>> DyThreadHashtagsMap = new HashMap<>();
        for (dyThreadHashtag dyThreadHashtag : dyThreadHashtags) {
            Integer dyThreadId = dyThreadHashtag.getDyThreadId();
            DyHashtag DyHashtag = DyHashtagMap.get(dyThreadHashtag.getDyHashtagId());
            if (DyHashtag != null) {
                DyThreadHashtagsMap.computeIfAbsent(dyThreadId, k -> new ArrayList<>()).add(DyHashtag);
            }
        }
        return DyThreadHashtagsMap;
    }
    private Map<Integer, DyHashtag> getDyHashtagMap(Set<Integer> dyHashtagIds) {
        QueryWrapper<DyHashtag> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("dyHashtagId", dyHashtagIds);
        List<DyHashtag> dyHashtags = dyHashtagDao.selectList(queryWrapper);
        Map<Integer, DyHashtag> dyHashtagMap = new HashMap<>();
        for (DyHashtag DyHashtag : dyHashtags) {
            dyHashtagMap.put(DyHashtag.getDyHashtagId(), DyHashtag);
        }
        return dyHashtagMap;
    }
    private Map<Integer, Long> getReplyCountMap(List<DyThread> dyThreads) {
        List<Integer> dyThreadIds = dyThreads.stream().map(DyThread::getDyThreadId).collect(Collectors.toList());
        List<DyReply> replies = dyReplyDao.selectList(new QueryWrapper<DyReply>().in("dyThreadId", dyThreadIds));
        return replies.stream().collect(Collectors.groupingBy(DyReply::getDyReplyId, Collectors.counting()));
    }
}
