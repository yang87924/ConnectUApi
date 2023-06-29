package com.connectu.connectuapi.domain;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema
@Data
@TableName()
public class User  {
    @TableId
    private Integer userId;
    private String email;
//    @TableField(select = false)
    private String password;
    private String userName;
    private String level;
    private String avatar;
    private String profile;
    private String point;
    private String isGoogle;
    // 增加一個欄位用於存儲hotScore
    @TableField(exist = false)
    private Integer hotScore;
    // 增加一個欄位用於存儲該使用者的所有文章
    @TableField(exist = false)
    private List<Thread> threads;
}
