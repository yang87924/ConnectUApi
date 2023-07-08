package com.connectu.connectuapi.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Schema
@Data
public class Reply {
    @TableId
    private Integer replyId;
    private Integer threadId ;
    private Integer userId  ;
    private String content;
    private String createdAt;
    private Integer loveCount;
    @TableField(exist = false)
    private User user;
}
