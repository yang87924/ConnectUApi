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
    @ApiModelProperty(value = "總分數")
    private Integer hotScore;
    private Integer loveStatus;
    @ApiModelProperty(value = "收藏數量")
    private Integer favoriteCount;
    @ApiModelProperty(value = "replyCount")
    @TableField(exist = false)
    private Integer replyCount=0;
    @TableField(exist = false)
    private List<DyHashtag> hashtags;


    @TableField(exist = false)
    @ApiModelProperty(value = "user")
    private User user;
    public void updateLoveStatus(Integer loveStatus) {
        this.loveStatus = loveStatus;
    }

    @TableField(exist = false)
    private String[] pictureArray; // Use this field instead of 'PictureArray'
    public void setPicture(String picture) {
        this.picture = picture;
        this.pictureArray = picture.split("▲");
    }

    public String[] getPictureArray() {
        return pictureArray;
    }

    public void setPictureArray(String[] pictureArray) {
        this.pictureArray = pictureArray;
    }

}
