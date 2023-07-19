package com.connectu.connectuapi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
@TableName("dyHashtag")
public class DyHashtag {
    @TableId
    private Integer dyHashtagId;
    private String name;
    private Integer amount ;
}
