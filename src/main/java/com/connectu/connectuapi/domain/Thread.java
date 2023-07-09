package com.connectu.connectuapi.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Schema
@Data
@ApiModel(description = "論壇文章")
public class Thread  {
    @TableId
    @ApiModelProperty(value = "論壇文章 ID")
    @Column(name = "threadId")
    private Integer threadId;
    @ApiModelProperty(value = "分類 ID")
    private Integer categoryId;
    @TableField(exist = false)
    @ApiModelProperty(value = "分類名稱")
    private String categoryName;
    @ApiModelProperty(value = "使用者 ID")
    private Integer userId ;
    @ApiModelProperty(value = "標題")
    private String title;
    @ApiModelProperty(value = "內容")
    private String content;
    @ApiModelProperty(value = "建立時間")
    private String createdAt;
    @ApiModelProperty(value = "圖片")
    private String picture;
    @ApiModelProperty(value = "讚數")
    private Integer love ;
    @ApiModelProperty(value = "收藏數量")
    private Integer favoriteCount;
    @ApiModelProperty(value = "總分數")
    private Integer hotScore;
    private Integer loveStatus;
    @TableField(exist = false)
    @ApiModelProperty(value = "replyCount")
    private Integer replyCount=0;
    @TableField(exist = false)
    @ApiModelProperty(value = "user")
    private User user;

    @TableField(exist = false)
    private List<Hashtag> hashtags;
    @TableField(exist = false)
    private String[] pictureArray; // Use this field instead of 'PictureArray'
    public void setPicture(String picture) {
        this.picture = picture;
        this.pictureArray = picture.split("▲");
    }

    public String[] getPictureArray() {
        return pictureArray;
    }
    public void updateLoveStatus(Integer loveStatus) {
        this.loveStatus = loveStatus;
    }
    public void setHashtags(List<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    public List<Hashtag> getHashtags() {
        return hashtags;
    }


}