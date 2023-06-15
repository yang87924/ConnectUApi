package com.connectu.connectuapi.domain;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
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
}
