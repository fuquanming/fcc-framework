/*
 * @(#)UserController.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-framework
 * 创建日期 : 2019年3月27日
 * 修改历史 : 
 *     1. [2019年3月27日]创建文件 by 傅泉明
 */
package com.fcc.framework.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fcc.framework.annotation.Autowired;
import com.fcc.framework.annotation.Controller;
import com.fcc.framework.annotation.RequestMapping;
import com.fcc.framework.annotation.RequestParam;
import com.fcc.framework.demo.model.User;
import com.fcc.framework.demo.service.UserService;

/**
 * 
 * @version 
 * @author 傅泉明
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @RequestMapping("/get")
    public List<User> getUser(@RequestParam("id") String[] id, HttpServletRequest request,
            HttpServletResponse response) {
        System.out.println("UserController.getUser.id=" + Arrays.toString(id));
        List<User> userList = Collections.emptyList();
        if (id != null) {
            userList = new ArrayList<User>();
            for (String ids : id) {
                User user = userService.getUserById(ids);
                if (user != null) {
                    userList.add(user);
                }
            }
        }
        return userList;
    }
    
}
