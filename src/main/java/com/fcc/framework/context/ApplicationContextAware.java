/*
 * @(#)ApplicationContextAware.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-framework-v2
 * 创建日期 : 2019年4月30日
 * 修改历史 : 
 *     1. [2019年4月30日]创建文件 by 傅泉明
 */
package com.fcc.framework.context;

/**
 * 通过解耦方式获得 IOC 容器的顶层设计
 * 后面将通过一个监听器去扫描所有的类，只要实现了此接口，
 * 将自动调用 setApplicationContext()方法，从而将 IOC 容器注入到目标类中
 * @version 
 * @author 傅泉明
 */
public interface ApplicationContextAware {
    void setApplicationContext(ApplicationContext applicationContext);
}
