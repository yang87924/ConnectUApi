package com.connectu.connectuapi.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Schema
@Data
public class Thread  {
    @TableId
    private Integer threadId;
    private Integer categoryId;
    private Integer userId ;
    private String title;
    private String content;
    private String createdAt;
    private String picture;

}
