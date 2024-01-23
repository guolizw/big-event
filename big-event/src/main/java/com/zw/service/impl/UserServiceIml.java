package com.zw.service.impl;

import com.zw.mapper.UserMapper;
import com.zw.pojo.User;
import com.zw.service.UserService;
import com.zw.utils.Md5Util;
import com.zw.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;


@Service
public class UserServiceIml implements UserService {

    @Autowired
    private UserMapper userMapper;
    //根据 用户名查询用户
    @Override
    public User findUserByName(String username) {
        User user = userMapper.findUserByName(username);
        return user;
    }

    //注册
    @Override
    public void register(String username, String password) {
        //先进性对密码的加密
        String md5String = Md5Util.getMD5String(password);
        //添加
        userMapper.addUser(username,md5String);
    }

    @Override
    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());
        userMapper.update(user);
    }

    //更新头像
    @Override
    public void updateAvatar(String avatarUrl) {
        //从ThreadLocal获取用户的id 方便进行update
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id =(Integer) map.get("id");
        userMapper.updateAvatar(avatarUrl,id);
    }

    //修改密码
    @Override
    public void updatePwd(String newPwd, String username) {
        userMapper.updatePwd(Md5Util.getMD5String(newPwd),username);
    }
}
