package com.connectu.connectuapi.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@ToString
public class UserThreadDTO {
    private Thread thread;
    private User user;
    private ThreadHashtag threadHashtag;
    private Hashtag hashtag;
    private String categoryName;
}
