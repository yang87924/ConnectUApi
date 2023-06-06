package com.connectu.connectuapi.domain;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("userInfo")
public class UserInfo {
    @TableId
    private Integer userId;
    private String level;
    private Object headshot;
    private String profile;
    private String point;
}
