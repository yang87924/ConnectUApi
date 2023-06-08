package com.connectu.connectuapi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Schema
@Data
@TableName("dyThread")
public class DyThread {
    @TableId
    private Integer threadId;
    private Integer userId ;
    private String title;
    private String content;
    private String createdAt;
    private Object picture;

}
