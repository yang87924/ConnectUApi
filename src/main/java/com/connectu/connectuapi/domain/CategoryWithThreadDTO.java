package com.connectu.connectuapi.domain;

import lombok.Data;

import java.util.List;

@Data
public class CategoryWithThreadDTO {
    private Category category;
    private List<Thread> threadList;
    private Integer threadCount;
}

