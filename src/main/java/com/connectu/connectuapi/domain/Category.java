package com.connectu.connectuapi.domain;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
@Data
public class Category {
    @TableId
    private Integer categoryId ;
    private String 	categoryName;
}
