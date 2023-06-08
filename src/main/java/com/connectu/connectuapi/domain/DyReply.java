package com.connectu.connectuapi.domain;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Schema
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
