package com.connectu.connectuapi.controller;

import com.connectu.connectuapi.controller.util.Code;
import com.connectu.connectuapi.controller.util.Result;
import com.connectu.connectuapi.domain.Category;
import com.connectu.connectuapi.domain.Thread;
import com.connectu.connectuapi.service.ICategoryService;
import com.connectu.connectuapi.service.impl.CategoryServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.connectu.connectuapi.domain.Thread;
import java.util.List;

@Api(tags ="主題")
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    @ApiOperation("主題列表")
    public Result getAll(){
        List<Category> category = categoryService.list();
        Integer code = category != null ? Code.GET_OK : Code.GET_ERR;
        String msg = category != null ? "所有論壇文章資料成功" : "查無論壇文章資料";
        return new Result(code, category, msg);
    }
    @GetMapping("/withCateGoryThreadCount")
    @ApiOperation("獲取所有Category以及每個Category對應的Thread數量")
    public Result getAllWithThreadCount(){
        List<Category> categories = categoryService.getCategoriesWithThreadCount();
        Integer code = categories != null && !categories.isEmpty() ? Code.GET_OK : Code.GET_ERR;
        String msg = categories != null && !categories.isEmpty() ? "獲取所有Category以及每個Category對應的Thread數量" : "查無數據";
        return new Result(code, categories, msg);
    }

}
