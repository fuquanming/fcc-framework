/*
 * @(#)BeanWrapper.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-framework-v2
 * 创建日期 : 2019年4月30日
 * 修改历史 : 
 *     1. [2019年4月30日]创建文件 by 傅泉明
 */
package com.fcc.framework.beans;

import lombok.Data;

/**
 * 
 * @version 
 * @author 傅泉明
 */
@Data
public class BeanWrapper {
    private Object wrappedInstance;
    private Class<?> wrappedClass;
    
    public BeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }
    
    public Class<?> getWrappedClass() {
        return this.wrappedInstance == null ? null : this.wrappedInstance.getClass();
    }
}
