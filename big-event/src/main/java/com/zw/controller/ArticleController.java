package com.zw.controller;


import com.zw.pojo.Article;
import com.zw.pojo.PageBean;
import com.zw.pojo.Result;
import com.zw.service.ArticleService;
import com.zw.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.PushBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/list")
   public Result<String> list(/*@RequestHeader(name = "Authorization")String token, HttpServletResponse response*/){
        //如果正常执行，则意味成功 出现异常则为失败
//        try {
//            //验证token
//            Map<String, Object> claims = JwtUtil.parseToken(token);
            return Result.success("所有文章。。。");
//
//        }catch (Exception e){
//            response.setStatus(401);
//            return Result.error("未登录");
//        }

    }

    //新增文章
    @PostMapping
    public Result addArticle(@RequestBody @Validated Article article){
        articleService.addArticle(article);
        return Result.success();
    }

    //分页查询文章
    @GetMapping
    public Result<PageBean<Article>> showList(Integer pageNum,Integer pageSize,
                                              @RequestParam(required = false)Integer categoryId,
                                              @RequestParam(required = false)String state){
        PageBean<Article> pb = articleService.showList(pageNum,pageSize,categoryId,state);
        return Result.success(pb);
    }

    //展示文章详情
    @GetMapping("/detail")
    public Result<Article> showDetail(Integer id){
        Article article = articleService.showDetail(id);
        return Result.success(article);

    }

    //更新文章信息
    @PutMapping
    public Result updateArticle(@RequestBody @Validated Article article){
        articleService.updateArticle(article);
        return Result.success();
    }

    @DeleteMapping
    public Result deleteArticle(Integer id){
        articleService.deleteArticle(id);
        return Result.success();
    }

}
