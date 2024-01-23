package com.zw.service;


import com.zw.pojo.Article;
import com.zw.pojo.PageBean;


public interface ArticleService {
    void addArticle(Article article);

    //分页查询文章
    PageBean<Article> showList(Integer pageNum, Integer pageSize, Integer categoryId, String state);


    //展示文章详情
    Article showDetail(Integer id);

    void updateArticle(Article article);

    void deleteArticle(Integer id);
}
