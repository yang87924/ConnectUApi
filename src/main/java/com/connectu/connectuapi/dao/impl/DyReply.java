package com.connectu.connectuapi.dao.impl;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@Data
@TableName("dyReply")
public class DyReply {
    @TableId
    private Integer dyReplyId ;
    private Integer dyThreadId;
    private Integer userId;
    private String content;
    private String createdAt;
    private String picture;
}
