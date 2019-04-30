/*
 * @(#)ApplicationContext.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-framework-v2
 * 创建日期 : 2019年4月30日
 * 修改历史 : 
 *     1. [2019年4月30日]创建文件 by 傅泉明
 */
package com.fcc.framework.context;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fcc.framework.beans.BeanDefinition;
import com.fcc.framework.beans.BeanWrapper;
import com.fcc.framework.core.BeanFactory;

/**
 * IOC、DI、MVC、AOP
 * @version 
 * @author 傅泉明
 */
public class ApplicationContext extends DefaultListableBeanFactory implements BeanFactory {
    /** 配置信息路径 */
    private String[] configLoactions = null;
    private BeanDefinitionReader reader;
    /** 单例的IOC容器 */
    private Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>();
    /** 通用IOC容器 */
    private Map<String, BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<String, BeanWrapper>();
    
    public ApplicationContext(String... configLoactions) {
        this.configLoactions = configLoactions;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void refresh() throws Exception {
        // 1、定位，配置文件
        reader = new BeanDefinitionReader(this.configLoactions);
        // 2、加载配置文件，扫描类，并封装成BeanDefinition
        List<BeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
        // 3、注册、把配置信息放在容器里（IOC）
        doRegisterBeanDefinition(beanDefinitions);
        // 4、把不是延迟加载的类，提前初始化
        doAutowrited();
    }
    
    private void doAutowrited() {
        
    }

    private void doRegisterBeanDefinition(List<BeanDefinition> beanDefinitions) {
        
    }

    public Object getBean(String beanName) throws Exception {
        return null;
    }

    public Object getBean(Class<?> beanClass) throws Exception {
        return null;
    }

}
