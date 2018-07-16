package com.crimps.shiroskill.service;

import com.crimps.shiroskill.domain.entity.SysPermission;

import java.util.List;

public interface SysPermissionService {

    /**
     * 添加权限
     *
     * @param permission 权限字符串
     * @param url 资源路径
     */
    public void addSysPermission(String permission, String url);

    /**
     *  获取所有权限
     */
    public List<SysPermission> getAllPermission();
}
