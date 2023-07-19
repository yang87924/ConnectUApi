package com.connectu.connectuapi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("favoriteDyThread")
public class FavoriteDyThread {
    @TableId
    private Integer favoriteDyThreadId;
    private Integer userId;
    private Integer dyThreadId;
}
