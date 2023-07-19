package com.connectu.connectuapi.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("friendship")
public class Friendship {
    @TableId
    private Integer friendshipId;
    private Integer followerId;
    private Integer followingId;
    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private DyThread dyThread;
    @TableField(exist = false)
    private DyHashtag dyHashtag;

}

