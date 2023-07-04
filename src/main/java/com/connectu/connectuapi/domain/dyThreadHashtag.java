package com.connectu.connectuapi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema
@Data
@TableName("dyThreadHashtag")
public class dyThreadHashtag {
    @TableId
    private Integer dyThreadHashtagId ;
    private Integer dyThreadId ;
    private Integer dyHashtagId  ;
}
