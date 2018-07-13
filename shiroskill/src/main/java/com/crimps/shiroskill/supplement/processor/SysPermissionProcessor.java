package com.crimps.shiroskill.supplement.processor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

@Component
public class SysPermissionProcessor implements BeanPostProcessor {

    Logger logger = LoggerFactory.getLogger(SysPermissionProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        //权限根据@RequiresPermissions、@RequestMapping入库
        Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
        if (methods != null) {
            for (Method method : methods) {
                RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
                RequiresPermissions requiresPermissions = AnnotationUtils.findAnnotation(method, RequiresPermissions.class);
                if (requestMapping != null && requiresPermissions != null) {
                    logger.info("request path : " + requestMapping.path()[0]);
                    logger.info("add syspermission :" + requiresPermissions.value()[0]);
                }
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
