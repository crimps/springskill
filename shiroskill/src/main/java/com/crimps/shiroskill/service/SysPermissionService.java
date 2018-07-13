package com.crimps.shiroskill.service;

public interface SysPermissionService {

    /**
     * 添加权限
     * @param permission 权限字符串
     * @param url 资源路径
     */
    public void addSysPermission(String permission, String url);
}
