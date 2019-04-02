/*
 * @(#)UserService.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-framework
 * 创建日期 : 2019年3月27日
 * 修改历史 : 
 *     1. [2019年3月27日]创建文件 by 傅泉明
 */
package com.fcc.framework.demo.service;

import com.fcc.framework.demo.model.User;

/**
 * 
 * @version 
 * @author 傅泉明
 */
public interface UserService {
    User getUserById(String userId);
}
