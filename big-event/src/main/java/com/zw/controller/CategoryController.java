package com.zw.controller;


import com.zw.pojo.Category;
import com.zw.pojo.Result;
import com.zw.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    //添加文章分类
    @PostMapping
    public Result addCategory(@RequestBody @Validated Category category){
        categoryService.addCategory(category);
        return Result.success();
    }

    //查看文章列表
    @GetMapping
    public Result<List<Category>> showCategory(){
        List<Category> list = categoryService.showCategory();
        return Result.success(list);
    }

    //根据id查询具体的文章分类详情
    @GetMapping("/detail")
    public Result<Category> showDetail(Integer id){
        Category category = categoryService.showDetail(id);
        return Result.success(category);
    }

    //更新文章分类
    @PutMapping
    public Result updateCategory(@RequestBody @Validated(Category.update.class) Category category){
        categoryService.updateCategory(category);
        return Result.success();

    }

    //删除文章分类
    @DeleteMapping
    public Result deleteCategory(Integer id){
        categoryService.deleteCategory(id);
        return Result.success();
    }
}
