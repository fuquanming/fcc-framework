/*
 * @(#)BeanDefinitionReader.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-framework-v2
 * 创建日期 : 2019年4月30日
 * 修改历史 : 
 *     1. [2019年4月30日]创建文件 by 傅泉明
 */
package com.fcc.framework.context;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import com.fcc.framework.beans.BeanDefinition;

/**
 * 读取配置文件
 * @version 
 * @author 傅泉明
 */
public class BeanDefinitionReader {

    private List<String> registyBeanClasses = new ArrayList<String>();
    private Properties config = new Properties();
    /** 配置文件中扫描类的Key， */
    private final String SCAN_PACKAGE = "scanPackage";
    
    public BeanDefinitionReader(String... configLoactions) {
        // 找到文件，并转化为文件流
        InputStream is = null;
        try {
            is = this.getClass().getClassLoader().getResourceAsStream(configLoactions[0].replace("classpath:", ""));
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
        }
        doScanner(config.getProperty(SCAN_PACKAGE));
    }
    
    private void doScanner(String scanPackage) {
        System.out.println("load SCAN_PACKAGE=" + scanPackage);
        // 将类的.替换为文件目录
        String filePath = "/" + scanPackage.replaceAll("\\.", "/");
        URL url = this.getClass().getClassLoader().getResource(filePath);
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (!file.getName().endsWith(".class")) continue;
                String className = (scanPackage + "." + file.getName().replace(".class", ""));
                registyBeanClasses.add(className);
                System.out.println("load className=" + className);
            }
        }
    }
    
    public Properties getConfig() {
        return this.config;
    }
    /** 将配置信息的类，转换为BeanDefinition，之后IOC操作 */
    public List<BeanDefinition> loadBeanDefinitions() {
        List<BeanDefinition> list = new ArrayList<BeanDefinition>();
        try {
            for (String className : registyBeanClasses) {
                Class<?> beanClass = Class.forName(className);
                if (beanClass.isInterface()) continue;
                list.add(doCreateBeanDefinition(
                        toLowerFirstCase(beanClass.getSimpleName()), beanClass.getName()
                        ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    /** 将类转换为BeanDefinition */
    private BeanDefinition doCreateBeanDefinition(String factoryBeanName, String beanClassName) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }

    private String toLowerFirstCase(String simpleName) {
        char [] chars = simpleName.toCharArray();
        //之所以加，是因为大小写字母的ASCII码相差32，
        // 而且大写字母的ASCII码要小于小写字母的ASCII码
        //在Java中，对char做算学运算，实际上就是对ASCII码做算学运算
        chars[0] += 32;
        return String.valueOf(chars);
    }
    
}

