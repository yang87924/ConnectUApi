package com.connectu.connectuapi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
@TableName("hashtag")
public class Hashtag {
    @TableId
    private Integer hashtagId;
    private String name;
    private Integer amount ;
    private String picture;
}
