package com.connectu.connectuapi.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
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
    @TableField(exist = false)
    @ApiModelProperty(value = "分類名稱")
    private String categoryName;
//    @TableField(exist = false)
//    @ApiModelProperty(value = "分類信息")
//    private Category category;
}