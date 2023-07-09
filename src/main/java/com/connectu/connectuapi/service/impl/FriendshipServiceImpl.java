package com.connectu.connectuapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.connectu.connectuapi.dao.*;
import com.connectu.connectuapi.domain.*;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.service.IDyHashtagService;
import com.connectu.connectuapi.service.IDyThreadService;
import com.connectu.connectuapi.service.IFriendshipService;
import com.connectu.connectuapi.service.IUserService;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FriendshipServiceImpl extends MPJBaseServiceImpl<FriendshipDao, Friendship> implements IFriendshipService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private FriendshipDao friendshipDao;
    @Autowired
    private DyThreadDao dyThreadDao;
    @Autowired
    private IDyThreadService iDyThreadService;

    @Autowired
    private DyThreadHashtagDao dyThreadHashtagDao;
    @Autowired
    private DyHashtagDao dyHashtagDao;







    public List<User> following(Integer followingId){
        MPJLambdaWrapper<User> userWrapper = new MPJLambdaWrapper<>();
        userWrapper.innerJoin(Friendship.class, Friendship::getFollowerId, User::getUserId)
                .eq(Friendship::getFollowingId, followingId);
        return userDao.selectList(userWrapper);
    }

    public String followingNum(Integer followingId){
        LambdaQueryWrapper<Friendship> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Friendship::getFollowingId, followingId);
        User user=userDao.selectById(followingId);
        user.setFollowingCount(friendshipDao.selectCount(lqw).intValue());
        userDao.updateById(user);
        return friendshipDao.selectCount(lqw).toString();
    }


    public List<User> follower(Integer followerId){
        MPJLambdaWrapper<User> userWrapper = new MPJLambdaWrapper<>();
        userWrapper.innerJoin(Friendship.class, Friendship::getFollowingId, User::getUserId)
                .eq(Friendship::getFollowerId, followerId);
        return userDao.selectList(userWrapper);
    }


    public String followerNum(Integer followerId){
        LambdaQueryWrapper<Friendship> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Friendship::getFollowerId, followerId);
        User user=userDao.selectById(followerId);
        user.setFollowedByCount(friendshipDao.selectCount(lqw).intValue());
        userDao.updateById(user);
        return friendshipDao.selectCount(lqw).toString();
    }

    @Override
    public List followingDyThread(Integer userId) {
        MPJLambdaWrapper<Friendship> userWrapper = new MPJLambdaWrapper<>();
        userWrapper
                .selectAll(User.class)
                //.selectAll(DyThread.class)
                // .selectAll(dyThreadHashtag.class)
                .selectAll(Friendship.class)
                // .selectAll(User.class)
                //  .selectAll(DyHashtag.class)
                .innerJoin(User.class,User::getUserId,Friendship::getFollowerId)
                //.innerJoin(DyThread.class,DyThread::getUserId,Friendship::getFollowerId)
                // .innerJoin(dyThreadHashtag.class,dyThreadHashtag::getDyThreadId,DyThread::getDyThreadId)
                // .innerJoin(DyHashtag.class,DyHashtag::getDyHashtagId,dyThreadHashtag::getDyHashtagId)
                .eq(Friendship::getFollowingId, userId);
        System.out.println(friendshipDao.selectList(userWrapper));
        //System.out.println("----------"+dythreads);
        return friendshipDao.selectList(userWrapper);
    }

    @Override
    public List<Friendship> getFirendShipThread(Integer userId, Page<Friendship> page) {
        QueryWrapper<Friendship> friendshipQuery = new QueryWrapper<>();
        friendshipQuery.eq("followingId", userId);

        // 应用分页查询
        Page<Friendship> friendshipPage = friendshipDao.selectPage(page, friendshipQuery);
        List<Friendship> friendships = friendshipPage.getRecords();

        for (Friendship friendship : friendships) {
            Integer followerId = friendship.getFollowerId();

            User user = userDao.selectById(followerId);
            friendship.setUser(user);

            QueryWrapper<DyThread> dyThreadQuery = new QueryWrapper<>();
            dyThreadQuery.eq("userId", followerId);
            dyThreadQuery.last("limit 4"); // 只查询每个用户的前4条动态线程数据
            List<DyThread> dyThreads = dyThreadDao.selectList(dyThreadQuery);

            for (DyThread dyThread : dyThreads) {
                friendship.setDyThread(dyThread);

                QueryWrapper<dyThreadHashtag> dyThreadHashtagQuery = new QueryWrapper<>();
                dyThreadHashtagQuery.eq("dyThreadId", dyThread.getDyThreadId());
                List<dyThreadHashtag> dyThreadHashtags = dyThreadHashtagDao.selectList(dyThreadHashtagQuery);

                List<DyHashtag> dyHashtags = new ArrayList<>();
                for (dyThreadHashtag dyThreadHashtag : dyThreadHashtags) {
                    Integer dyHashtagId = dyThreadHashtag.getDyHashtagId();
                    DyHashtag dyHashtag = dyHashtagDao.selectById(dyHashtagId);
                    dyHashtags.add(dyHashtag);
                }

                dyThread.setHashtags(dyHashtags);
            }
        }

        return friendships;
    }

    @Override
    public void getFriendShipStatus(Integer followerId, Integer followingId) {
        LambdaQueryWrapper<Friendship> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Friendship::getFollowerId, followerId)
                .eq(Friendship::getFollowingId, followingId);
        List<Friendship> friendships = friendshipDao.selectList(lqw);
        User user=new User();
        user.setUserId(followingId);
        if(friendships.size()!=0){
            user.setFriendshipStatus(1);
        }
        else {
            user.setFriendshipStatus(0);
        }
        userDao.updateById(user);
    }


    public boolean saveOrRemove(Integer followerId, Integer followingId) {
        LambdaQueryWrapper<Friendship> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Friendship::getFollowerId, followerId)
                .eq(Friendship::getFollowingId, followingId);
        List<Friendship> friendships = friendshipDao.selectList(lqw);
        if (friendships.size() != 0) {
            System.out.println("not null..." + friendships.size());
            super.removeByIds(friendshipDao.selectList(lqw));
            return false;
        }
        Friendship friendship = new Friendship();
        friendship.setFollowerId(followerId);
        friendship.setFollowingId(followingId);
        return super.save(friendship);
    }


}