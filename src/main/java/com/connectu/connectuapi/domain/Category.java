package com.connectu.connectuapi.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;

@Schema
@Data
@TableName("category")
public class Category {
    @TableId
    @Column(name = "categoryId")
    private Integer categoryId;
    private String categoryName;
    private String picture;
    @TableField(exist = false) // This field does not exist in the database
    private Integer threadCount; // This field will store the count of threads for this category
}
