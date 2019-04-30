/*
 * @(#)DefaultListableBeanFactory.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-framework-v2
 * 创建日期 : 2019年4月30日
 * 修改历史 : 
 *     1. [2019年4月30日]创建文件 by 傅泉明
 */
package com.fcc.framework.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fcc.framework.beans.BeanDefinition;

/**
 * 
 * @version 
 * @author 傅泉明
 */
public class DefaultListableBeanFactory extends AbstractApplicationContext {
    /**
     * 存储注册的Bean信息
     */
    protected final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();
}
