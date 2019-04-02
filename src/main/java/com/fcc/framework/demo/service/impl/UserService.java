/*
 * @(#)UserService.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-framework
 * 创建日期 : 2019年3月28日
 * 修改历史 : 
 *     1. [2019年3月28日]创建文件 by 傅泉明
 */
package com.fcc.framework.demo.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fcc.framework.annotation.Service;
import com.fcc.framework.demo.model.User;

/**
 * 
 * @version 
 * @author 傅泉明
 */
@Service
public class UserService implements com.fcc.framework.demo.service.UserService {

    static Map<String, User> userMap = new HashMap<String, User>();
    static {
        User user = new User();
        user.setUserId("1");
        user.setUsername("李四");
        user.setBirthDay(new Date());
        userMap.put("1", user);
        user = new User();
        user.setUserId("2");
        user.setUsername("王五");
        user.setBirthDay(new Date());
        userMap.put("2", user);
    }
    
    public User getUserById(String userId) {
        return userMap.get(userId);
    }

}
