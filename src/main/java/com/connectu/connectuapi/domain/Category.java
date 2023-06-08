package com.connectu.connectuapi.domain;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Schema
@Data
public class Category {
    @TableId
    private Integer categoryId ;
    private String 	categoryName;
}
