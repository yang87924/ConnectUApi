package com.connectu.connectuapi.dao.impl;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
@Data
public class Thread  {
    @TableId
    private Integer threadId;
    private Integer categoryId;
    private Integer userId ;
    private String title;
    private String content;
    private String createdAt;
    private Object picture;

}
