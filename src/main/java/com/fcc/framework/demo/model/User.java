/*
 * @(#)User.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-framework
 * 创建日期 : 2019年3月27日
 * 修改历史 : 
 *     1. [2019年3月27日]创建文件 by 傅泉明
 */
package com.fcc.framework.demo.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @version 
 * @author 傅泉明
 */
public class User {
    private String userId;
    private String username;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date birthDay;
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Date getBirthDay() {
        return birthDay;
    }
    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }
}
