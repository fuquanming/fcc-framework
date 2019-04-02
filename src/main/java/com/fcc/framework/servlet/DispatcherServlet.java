/*
 * @(#)DispatcherServlet.java
 * 
 * Copyright (c) 2015, All Rights Reserved
 * 项目名称 : fcc-framework
 * 创建日期 : 2019年3月27日
 * 修改历史 : 
 *     1. [2019年3月27日]创建文件 by 傅泉明
 */
package com.fcc.framework.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.fcc.framework.annotation.Autowired;
import com.fcc.framework.annotation.Controller;
import com.fcc.framework.annotation.RequestMapping;
import com.fcc.framework.annotation.RequestParam;
import com.fcc.framework.annotation.Service;

/**
 * 模拟Spring MVC DispatcherServlet
 * @version 
 * @author 傅泉明
 */
public class DispatcherServlet extends HttpServlet {
    /**
     * 
     */
    private static final long serialVersionUID = 2646269519405198155L;
    /** 加载的配置信息 */
    Properties contextConfig = new Properties();
    /** 扫描的类名 */
    List<String> classNames = new ArrayList<String>();
    /** IOC，已实例化的类,key:首字母小写 */
    Map<String, Object> ioc = new HashMap<String, Object>();
    /** 保存url和Method的对应关系 */
    Map<String, Method> handlerMapping = new HashMap<String, Method>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // 1、加载配置信息
        loadConfig(config.getInitParameter("contextConfigLocation"));
        // 2、扫描相关的类
        loadScanner(contextConfig.getProperty("scanPackage"));
        // 3、初始化扫描到的类并实例化到IOC容器中
        loadInstance();
        // 4、依赖注入
        loadAutowired();
        // 5、初始化HandlerMapping，完成路由映射
        loadHandlerMapping();
        System.out.println("-------------fcc framework is init-------------");
    }
    /** 初始化HandlerMapping，完成路由映射RequestMapping,及参数映射RequestParam */
    private void loadHandlerMapping() {
        if (ioc.isEmpty()) return;
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();
            if (!clazz.isAnnotationPresent(Controller.class)) continue;
            // 获取类的RequestMapping上配置的地址
            String baseUrl = "";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                baseUrl = clazz.getAnnotation(RequestMapping.class).value();
            }
            // 获取public方法上RequestMapping配置的地址
            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(RequestMapping.class)) continue;
                String methodUrl = method.getAnnotation(RequestMapping.class).value();
                String baseMethodUrl = ("/" + baseUrl + "/" + methodUrl).replaceAll("/+", "/");
                handlerMapping.put(baseMethodUrl, method);
                System.out.println(baseMethodUrl + ":" + method);
            }
        }
    }
    /** 依赖注入Autowired标记 */
    private void loadAutowired() {
        if (ioc.isEmpty()) return;
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            //Declared 所有的，特定的 字段，包括private/protected/default
            //正常来说，普通的OOP编程只能拿到public的属性
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    // 是否自定义名称
                    String beanName = autowired.value();
                    if (StringUtils.isEmpty(beanName)) {
                        // 用字段名称
                        beanName = field.getName();
                    }
                    
                    // 字段名称不存在则用接口的名称
                    if (!ioc.containsKey(beanName)) {
                        beanName = toLowerFirstCase(field.getType().getSimpleName());
                    }

                    //如果是public以外的修饰符，只要加了@Autowired注解，都要强制赋值
                    //反射中叫做暴力访问
                    field.setAccessible(true);
                    try {
                        //用反射机制，动态给字段赋值
                        field.set(entry.getValue(), ioc.get(beanName));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    /** 初始化扫描到的类并实例化到IOC容器中 */
    private void loadInstance() {
        if (classNames.isEmpty()) return;
        ioc.clear();
        for (String className : classNames) {
            try {
                // 实例化有标记的类，如Controller，Service
                Class<?> clazz = Class.forName(className);
                if (clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class)) {
                    Object instance = clazz.newInstance();
                    // 如果标记有设置指定的名称
                    Controller controller = clazz.getAnnotation(Controller.class);
                    Service service = clazz.getAnnotation(Service.class);
                    String defaultBeanName = "";
                    if (controller != null) {
                        defaultBeanName = controller.value();
                    }
                    if (service != null) {
                        defaultBeanName = service.value();
                    }
                    if (StringUtils.isEmpty(defaultBeanName)) {
                        // 没有指定名称，类的首字母小写
                        defaultBeanName = toLowerFirstCase(clazz.getSimpleName());
                    }
                    // 名称不能重复
                    if (ioc.containsKey(defaultBeanName)) {
                        throw new RuntimeException("instance exist=" + defaultBeanName + ":" + ioc.get(defaultBeanName));
                    }
                    // 类名做key
                    ioc.put(defaultBeanName, instance);
                    System.out.println("instance=" + defaultBeanName + ":" + instance);
                    // 如果有接口，则将接口的名称单做key
                    // 1、如类UserService并自定名称UserService,UserServiceImpl都实现了接口UserService
                    // 2、UserService,则有名称userService,UserService;UserServiceImpl,则有userServiceImpl
                    Class<?>[] clazzs = clazz.getInterfaces();
                    for (Class<?> c : clazzs) {
                        String beanName = toLowerFirstCase(c.getSimpleName());
                        if (ioc.containsKey(beanName)) {
                            //System.err.println("instance exits=" + beanName + ":" + instance);                            
                            continue;
                        }
                        System.out.println("instance=" + beanName + ":" + instance);
                        ioc.put(beanName, instance);
                    }
                }                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
    }
    /** 扫描相关的类 */
    private void loadScanner(String scanner) {
        URL url = DispatcherServlet.class.getClassLoader().getResource("/" + scanner.replaceAll("\\.", "/"));
        File file = new File(url.getFile());
        File[] files = file.listFiles();
        if (files == null) return;
        for (File f : files) {
            if (f.isDirectory()) {
                loadScanner(scanner + "." + f.getName());
            } else {
                String filename = f.getName();
                if (filename.endsWith(".class")) {
                    classNames.add(scanner + "." + filename.replace(".class", ""));
                    System.out.println("-----------------loadClass=" + scanner + "." + filename.replace(".class", ""));
                }
            }
        }
    }
    /** 加载配置文件信息 */
    private void loadConfig(String contextConfigLocation) {
        InputStream is = null;
        try {
            is = DispatcherServlet.class.getClassLoader().getResourceAsStream(contextConfigLocation);
            contextConfig.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    private String toLowerFirstCase(String simpleName) {
        char [] chars = simpleName.toCharArray();
        //之所以加，是因为大小写字母的ASCII码相差32，
        // 而且大写字母的ASCII码要小于小写字母的ASCII码
        //在Java中，对char做算学运算，实际上就是对ASCII码做算学运算
        chars[0] += 32;
        return String.valueOf(chars);
    }
    
    @Override
    public void destroy() {
        super.destroy();
    }        
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contextPath = req.getContextPath();
        String uri = req.getRequestURI().replaceAll("/+", "/");
        if (StringUtils.isNotEmpty(contextPath)) uri = uri.replaceAll(contextPath, "");
//        String url = req.getRequestURL().toString();// 完成请求路径
        resp.setCharacterEncoding("UTF-8");     
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date()) + "\r\nuri=" + uri 
//                + "\r\nurl=" + url
                );
        if (handlerMapping.containsKey(uri)) {
            // 找到Controller的Method
            Method method = handlerMapping.get(uri);
            // 给方法传参数
            Class<?>[] parameterTypes = method.getParameterTypes();
            int length = parameterTypes.length;
            Object[] paramObj = new Object[length];
            for (int i = 0; i < length; i++) {
                Class<?> c = parameterTypes[i];
                // System.out.println(c.getName());
                // 是否有标记RequestParam
                Annotation[] annotations = method.getParameterAnnotations()[i];
                for (Annotation a : annotations) {
                    if (a.annotationType() == RequestParam.class) {
                        // 获取参数名称
                        String paramName = ((RequestParam)a).value();
                        if (c == String[].class) {// 数组
                            paramObj[i] = req.getParameterValues(paramName);                            
                        } else {
                            paramObj[i] = req.getParameter(paramName);                            
                        }
                    }
                }
                if (c == HttpServletRequest.class) {
                    paramObj[i] = req;
                } else if (c == HttpServletResponse.class) {
                    paramObj[i] = resp;
                }
            }
            // 执行method
            try {
                String controller = toLowerFirstCase(method.getDeclaringClass().getSimpleName());
                // 获取method所在的对象
                Object methodObj = ioc.get(controller);
                Object returnObj = method.invoke(methodObj, paramObj);
                if (returnObj != null) {
                    //resp.setContentType("application/json");// 设置json输出
                    resp.setContentType("text/html;charset=UTF-8");// 设置json输出
                    resp.getWriter().println(JSONObject.toJSONString(returnObj));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    class Handler {
        String url;
        Method method;
        Object controller;
        public Handler(String url, Method method, Object controller) {
            super();
            this.url = url;
            this.method = method;
            this.controller = controller;
        }
        
    }
}
