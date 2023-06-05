package com.connectu.connectuapi.domain;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
@Data
public class Reply {
    @TableId
    private Integer replyId;
    private Integer threadId ;
    private Integer userId  ;
    private String content;
    private String createdAt;
}
