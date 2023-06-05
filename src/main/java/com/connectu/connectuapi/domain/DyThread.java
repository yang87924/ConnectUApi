package com.connectu.connectuapi.domain;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@Data
@TableName("dyThread")
public class DyThread {
    @TableId
    private Integer threadId;
    private Integer userId ;
    private String title;
    private String content;
    private String createdAt;
    private Object picture;

}
