package com.zw.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zw.mapper.ArticleMapper;
import com.zw.pojo.Article;
import com.zw.pojo.PageBean;
import com.zw.service.ArticleService;
import com.zw.utils.ThreadLocalUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Override
    public void addArticle(Article article) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        article.setCreateUser(id);
        articleMapper.add(article);
    }

    @Override
    public PageBean<Article> showList(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        //创建PageBean对象
        PageBean<Article> pb = new PageBean<>();

        //开启分页查询
        PageHelper.startPage(pageNum,pageSize);

        //调用mapper  只需传入其余参数 上面两个参数会自动拼接
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id = (Integer) map.get("id");
        List<Article> list = articleMapper.showList(id,categoryId,state);
        //需转型page page中有获取总条数和获取当前页面数据的方法
        Page<Article> p = (Page<Article>) list;

        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());

        return pb;
    }

    //展示文章详情
    @Override
    public Article showDetail(Integer id) {
        return articleMapper.showDetail(id);
    }

    //更新文章信息
    @Override
    public void updateArticle(Article article) {
        articleMapper.update(article);
    }

    @Override
    public void deleteArticle(Integer id) {
        articleMapper.delete(id);
    }
}
