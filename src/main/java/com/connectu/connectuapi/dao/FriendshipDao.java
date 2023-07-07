package com.connectu.connectuapi.dao;


import com.connectu.connectuapi.domain.Friendship;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FriendshipDao extends MPJBaseMapper<Friendship> {
//    @Select("SELECT u.*, t.* ,c.*,th.*,h.* FROM friendship f " +
//            "JOIN user u ON f.followerId = u.userId " +
//            "JOIN thread t ON f.followerId = t.userId " +
//            "JOIN category c on t.categoryId=c.categoryId " +
//            "JOIN threadHashtag th on th.threadId=t.threadId " +
//            "JOIN hashtag h on th.hashtagId=h.hashtagId " +
//            "WHERE f.followingId = #{followingId}")
//     List<userDyThreadDTO> fetchFriendshipDetails(@Param("followingId") int followingId);
}
