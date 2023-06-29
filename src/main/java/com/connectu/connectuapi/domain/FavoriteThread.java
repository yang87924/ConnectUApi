package com.connectu.connectuapi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("favoriteThread")
public class FavoriteThread {
    @TableId
    private Integer favoriteThreadId;
    private Integer userId;
    private Integer threadId;
}
