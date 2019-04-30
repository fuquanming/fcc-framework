/*
 * @(#)AbstractApplicationContext.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-framework-v2
 * 创建日期 : 2019年4月30日
 * 修改历史 : 
 *     1. [2019年4月30日]创建文件 by 傅泉明
 */
package com.fcc.framework.context;

/**
 * IOC容器实现的顶层设计
 * @version 
 * @author 傅泉明
 */
public abstract class AbstractApplicationContext {
    /**
     * 受保护，只提供子类重写
     * @throws Exception
     */
    public void refresh() throws Exception{}
}
