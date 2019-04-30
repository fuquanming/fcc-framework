/*
 * @(#)BeanFactory.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-framework-v2
 * 创建日期 : 2019年4月30日
 * 修改历史 : 
 *     1. [2019年4月30日]创建文件 by 傅泉明
 */
package com.fcc.framework.core;

/**
 * 单例工厂
 * @version 
 * @author 傅泉明
 */
public interface BeanFactory {
    /**
     * 根据 beanName 从 IOC 容器中获得一个实例 Bean
     * @param beanName
     * @return
     * @throws Exception
     */
    Object getBean(String beanName) throws Exception;
    
    Object getBean(Class<?> beanClass) throws Exception;
    
}
