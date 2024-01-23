package com.zw.interceptors;

/*
编写拦截器 防止每一个Controller都要去验证token令牌
 */

import com.zw.pojo.Result;
import com.zw.utils.JwtUtil;
import com.zw.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;


@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        try {
            //从redis中获得相同的token
            ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
            String s = opsForValue.get(token);
            if (s==null){
                //token失效了
                throw  new RuntimeException();
            }
            //验证token
            Map<String, Object> claims = JwtUtil.parseToken(token);
            //在拦截器中将数据存入threadlocal中 避免多次从请求头中拿数据
            ThreadLocalUtil.set(claims);
            //放行
            return true;

        }catch (Exception e){
            response.setStatus(401);
            //不放行
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清除ThreadLocal 防止内存泄漏溢出
        ThreadLocalUtil.remove();
    }
}
