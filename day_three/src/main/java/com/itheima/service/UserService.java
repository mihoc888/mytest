package com.itheima.service;

import com.itheima.dao.UserDao;
import com.itheima.exception.UserMsgError;
import com.itheima.model.User;

public class UserService {
    private UserDao userDao = new UserDao();
    public UserService() {
    }


    public User getUserByUserName(String username) throws UserMsgError {
        //调用数据访问层
        return userDao.getUserByUserName(username);
    }

    public int register(User user) throws UserMsgError {
        //在一次进行账号验证,因为浏览器可以禁用js,这就导致了页面的验证失效了.
        String username = user.getUsername();
        if (username == null || username == "") {
            throw new UserMsgError("用户名不能为空");
        }
        return userDao.register(user);
    }

}
