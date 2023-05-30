package com.connectu.connectuapi.dao.impl;

import lombok.Data;

@Data
public class Reply {
    private Integer replyId;
    private Integer threadId ;
    private Integer userId  ;
    private String content;
    private String createdAt;
}
