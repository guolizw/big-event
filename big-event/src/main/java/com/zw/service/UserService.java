package com.zw.service;

import com.zw.pojo.User;

public interface UserService {

    User findUserByName(String username);

    void register(String username, String password);


    void update(User user);

    void updateAvatar(String avatarUrl);

    void updatePwd(String newPwd, String username);
}
