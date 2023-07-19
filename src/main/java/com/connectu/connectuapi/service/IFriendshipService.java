package com.connectu.connectuapi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.connectu.connectuapi.domain.Friendship;
import com.connectu.connectuapi.domain.User;
import com.github.yulichang.base.MPJBaseService;

import java.util.List;

public interface IFriendshipService extends MPJBaseService<Friendship> {
    public List<User> following(Integer followingId);
    public List<User> follower(Integer followerId);
    public boolean saveOrRemove(Integer followerId, Integer followingId);
    public String followingNum(Integer followingId);
    public String followerNum(Integer followerId);
    public List followingDyThread(Integer userId);
    public List<Friendship> getFirendShipThread(Integer userId, Page<Friendship> page);
    void getFriendShipStatus(Integer followerId, Integer followingId);
}
