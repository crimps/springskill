package com.crimps.shiroskill.service.shiro.impl;

import com.crimps.shiroskill.domain.entity.SysPermission;
import com.crimps.shiroskill.service.SysPermissionService;
import com.crimps.shiroskill.service.shiro.ShiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShiroServiceImpl implements ShiroService {

    @Autowired
    SysPermissionService sysPermissionService;
//    @Autowired
//    ShiroFilterFactoryBean shiroFilterFactoryBean;

    /**
     * 从数据库加载权限
     *
     * @return filterChainMap
     */
    @Override
    public Map<String, String> loadFilterChainDefinitions() {
        Map<String, String> filterChainDefinitionMap = new HashMap<>();
        // 配置不会被拦截的链接 从上向下顺序判断
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/templates/**", "anon");
        filterChainDefinitionMap.put("/login/validate", "anon");
        // 配置退出过滤器,具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");
        //添加用户操作权限
        List<SysPermission> sysPermissionList = sysPermissionService.getAllPermission();
        for (SysPermission sysPermission : sysPermissionList) {
            filterChainDefinitionMap.put(sysPermission.getUrl(), "authc,perms[" + sysPermission.getPermission() + "]");
        }
        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问【放行】-->
        filterChainDefinitionMap.put("/**", "authc");
        return filterChainDefinitionMap;
    }

//    /**
//     * 重新加载权限
//     */
//    @Override
//    public void updatePermission() {
//
//    }
}
