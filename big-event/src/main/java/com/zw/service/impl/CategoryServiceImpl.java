package com.zw.service.impl;

import com.zw.mapper.CategoryMapper;
import com.zw.pojo.Category;
import com.zw.service.CategoryService;
import com.zw.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class CategoryServiceImpl implements CategoryService {


    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public void addCategory(Category category) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        category.setCreateUser(id);
        categoryMapper.add(category);
    }

    @Override
    public List<Category> showCategory() {
        //查询当前用户分类里面的文章分类 需要从ThreadLocal中拿到用户信息
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        return categoryMapper.showCategory(id);
    }

    @Override
    public Category showDetail(Integer id) {
        return categoryMapper.showDetail(id);
    }

    @Override
    public void updateCategory(Category category) {
        categoryMapper.update(category);
    }

    @Override
    public void deleteCategory(Integer id) {
        categoryMapper.delete(id);
    }
}
