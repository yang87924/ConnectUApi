package com.connectu.connectuapi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
@TableName("threadHashtag")
public class ThreadHashtag {
    @TableId
    private Integer threadHashtagId ;
    private Integer threadId ;
    private Integer hashtagId  ;
}
