/*
 * @(#)BeanPostProcessor.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-framework
 * 创建日期 : 2019年4月30日
 * 修改历史 : 
 *     1. [2019年4月30日]创建文件 by 傅泉明
 */
package com.fcc.framework.beans.config;

/**
 * 
 * @version 
 * @author 傅泉明
 */
public class BeanPostProcessor {
    //为在 Bean 的初始化前提供回调入口
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }
    //为在 Bean 的初始化之后提供回调入口
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }
}
