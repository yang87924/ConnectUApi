package com.connectu.connectuapi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("friendship")
public class Friendship {
    @TableId
    private Integer friendshipId;
    private Integer followerId;
    private Integer followingId;
}

