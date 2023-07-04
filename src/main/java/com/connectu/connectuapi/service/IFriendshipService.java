package com.connectu.connectuapi.service;

import com.connectu.connectuapi.domain.Friendship;
import com.connectu.connectuapi.domain.User;
import com.github.yulichang.base.MPJBaseService;

import java.util.List;

public interface IFriendshipService extends MPJBaseService<Friendship> {
    public List<User> following(Integer followingId);
    public List<User> follower(Integer followerId);
    public boolean save(Integer followerId, Integer followingId);
    public boolean remove(Integer followerId, Integer followingId);
}
