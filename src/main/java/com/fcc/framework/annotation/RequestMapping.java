/*
 * @(#)RequestMapping.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-framework
 * 创建日期 : 2019年3月27日
 * 修改历史 : 
 *     1. [2019年3月27日]创建文件 by 傅泉明
 */
package com.fcc.framework.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
/**
 * 
 * @version 
 * @author 傅泉明
 */
public @interface RequestMapping {
    String value() default "";
}
