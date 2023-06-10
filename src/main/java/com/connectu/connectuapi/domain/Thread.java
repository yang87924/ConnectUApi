package com.connectu.connectuapi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Schema
@Data
@JsonIgnoreProperties({"threadId", "userId", "createdAt", "picture"})
@ApiModel(description = "論壇文章")
public class Thread  {
    @TableId
    @ApiModelProperty(value = "論壇文章 ID")
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

}
