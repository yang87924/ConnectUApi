package com.connectu.connectuapi.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema
@Data
@TableName("dyThread")
public class DyThread {
    @TableId
    private Integer dyThreadId ;
    private Integer userId ;

    private String content;
    private String createdAt;
    private String picture;
    private Integer love;
    @ApiModelProperty(value = "收藏數量")
    private Integer favoriteCount;
    @TableField(exist = false)
    private List<DyHashtag> hashtags;
    @ApiModelProperty(value = "replyCount")
    @TableField(exist = false)
    private Integer replyCount=0;
    @TableField(exist = false)
    @ApiModelProperty(value = "user")
    private User user;

}
