package com.crimps.shiroskill.service.shiro;

import java.util.Map;

public interface ShiroService {

    /**
     * 从数据库加载权限
     * @return filterChainMap
     */
    public Map<String, String> loadFilterChainDefinitions();
}
