package com.zw.service;

import com.zw.pojo.Category;

import java.util.List;

public interface CategoryService {
    void addCategory(Category category);

    List<Category> showCategory();

    Category showDetail(Integer id);

    void updateCategory(Category category);

    void deleteCategory(Integer id);
}
