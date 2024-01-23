package com.zw.controller;


import com.zw.pojo.Result;
import com.zw.pojo.User;
import com.zw.service.UserService;
import com.zw.utils.JwtUtil;
import com.zw.utils.Md5Util;
import com.zw.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//此注解可以将实体类转化为具体json数据 与@Controller的区别
@RestController
@RequestMapping("/user")
//@CrossOrigin
//采用validated 注解方式进行参数校验
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    //注册
    @PostMapping("/register")
    //必须是符合非空5-16位的字符串
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username ,@Pattern(regexp = "^\\S{5,16}$") String password){

        User user = userService.findUserByName(username);
        if(user==null){
            //没被占用 可以注册
            userService.register(username,password);
            return Result.success();
        }else {
            //被占用 返回被占用信息
            return Result.error("该用户已被注册");
        }

    }

    //登录
    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username ,@Pattern(regexp = "^\\S{5,16}$") String password){
        //先查寻用户是否存在
        User userLogin = userService.findUserByName(username);
        if(userLogin==null){
            return Result.error("不存在该用户");
        }

        //用户存在则查询密码是否一致
        if(Md5Util.getMD5String(password).equals(userLogin.getPassword())){

            //创建一个hashmap来存放数据
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("id", userLogin.getId());
            claims.put("username", userLogin.getUsername());
            //生成jwt令牌
            String token = JwtUtil.genToken(claims);
            //将jwt令牌放入redis中
            ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
            opsForValue.set(token, token,1, TimeUnit.HOURS);
            return Result.success(token);
        }


        return Result.error("密码错误");
    }

    //获取用户基本信息
    @GetMapping("/userInfo")
    public Result<User> userInfo(/*@RequestHeader(name = "Authorization") String token*/){
        //解码
//        Map<String, Object> claims = JwtUtil.parseToken(token);
        //从拦截器中的threadlocal中拿取数据
        Map<String, Object> claims = ThreadLocalUtil.get();
        String username =(String) claims.get("username");
        User user = userService.findUserByName(username);
        return Result.success(user);

    }

    //更新用户信息
    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user){
        userService.update(user);
        return Result.success();
    }


    @PatchMapping("updateAvatar")
    //通过传url地址来更新头像
    //@RequestParam注解可以是传过来的参数和方法上的参数不一致 但是需要在value属性与传过来的参数一致/
    public Result updateAvatar( @URL String avatarUrl){
        userService.updateAvatar(avatarUrl);
        return Result.success();

    }

    //更新密码
    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String,String> map, @RequestHeader( "Authorization") String token) {
        //拿去传来的密码并进行基础的非空校验
        String oldPwd = map.get("old_pwd");
        String newPwd = map.get("new_pwd");
        String rePwd = map.get("re_pwd");

        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd))
            return Result.error("缺少必要的参数");

        //比较原密码是否相同
        Map<String,Object> userMap = ThreadLocalUtil.get();
        String username = (String) userMap.get("username");
        User userCheck = userService.findUserByName(username);
        if(!userCheck.getPassword().equals(Md5Util.getMD5String(oldPwd)))
            return Result.error("原密码错误");

        //比较新密码和确认密码是否一致
        if(!newPwd.equals(rePwd))
            return Result.error("两次密码不一致");


        //密码必须符合5-16位非空字符
        if(!StringUtils.hasLength(newPwd)|| newPwd.length()<5 || newPwd.length()>16)
            return Result.error("密码必须是5-16位的非空字符");

        //经过层层校验 确定可以修改
        userService.updatePwd(newPwd,username);
        //并且删除redis中的旧的令牌
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        opsForValue.getOperations().delete(token);
        return Result.success();
    }
}
