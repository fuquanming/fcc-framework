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

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.fcc.framework.annotation.Autowired;
import com.fcc.framework.annotation.Controller;
import com.fcc.framework.annotation.Service;
import com.fcc.framework.beans.BeanDefinition;
import com.fcc.framework.beans.BeanWrapper;
import com.fcc.framework.beans.config.BeanPostProcessor;
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
    private Map<String, Object> singletonBeanCacheMap = new ConcurrentHashMap<String, Object>();
    /** 通用IOC容器 */
    private Map<String, BeanWrapper> beanWrapperMap = new ConcurrentHashMap<String, BeanWrapper>();
    
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
    //依赖注入，从这里开始，通过读取 BeanDefinition 中的信息
    //然后，通过反射机制创建一个实例并返回
    //Spring 做法是，不会把最原始的对象放出去，会用一个 BeanWrapper 来进行一次包装
    //装饰器模式：
    //1、保留原来的 OOP 关系
    //2、我需要对它进行扩展，增强（为了以后 AOP 打基础）
    public Object getBean(String beanName) throws Exception {
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        try{
            //生成通知事件
            BeanPostProcessor beanPostProcessor = new BeanPostProcessor();
            Object instance = instantiateBean(beanDefinition);
            if(null == instance){ return null;}
            //在实例初始化以前调用一次
            beanPostProcessor.postProcessBeforeInitialization(instance,beanName);
            BeanWrapper beanWrapper = new BeanWrapper(instance);
            this.beanWrapperMap.put(beanName,beanWrapper);
            //在实例初始化以后调用一次
            beanPostProcessor.postProcessAfterInitialization(instance,beanName);
            populateBean(beanName,instance);
            //通过这样一调用，相当于给我们自己留有了可操作的空间
            return this.beanWrapperMap.get(beanName).getWrappedInstance();
        }catch (Exception e){
            // e.printStackTrace();
            return null;
        }
    }

    public Object getBean(Class<?> beanClass) throws Exception {
        return null;
    }

    // 注入Controller，Service
    private void populateBean(String beanName, Object instance) {
        
        Class clazz = instance.getClass();
        if (!(clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class))) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Autowired.class)) continue;
            Autowired autowired = field.getAnnotation(Autowired.class);
            
            String autowireBeanName = autowired.value().trim();
            if (StringUtils.isEmpty(autowireBeanName)) {
                autowireBeanName = field.getType().getName();
            }
            field.setAccessible(true);
            try {
                field.set(instance, this.beanWrapperMap.get(autowireBeanName).getWrappedInstance());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    
    //传一个 BeanDefinition，就返回一个实例 Bean
    private Object instantiateBean(BeanDefinition beanDefinition){
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        try{
            //因为根据 Class 才能确定一个类是否有实例
            if(this.singletonBeanCacheMap.containsKey(className)){
                instance = this.singletonBeanCacheMap.get(className);
            }else{
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.singletonBeanCacheMap.put(beanDefinition.getFactoryBeanName(),instance);
            }
            return instance;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
