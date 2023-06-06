package com.connectu.connectuapi.domain;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
@Data
public class User  {
    @TableId
    private Integer userId;
    private String 	email;
    @TableField(select = false)
    private String password;
    private String userName;
}
