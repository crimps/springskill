package com.crimps.shiroskill.service.impl;

import com.crimps.shiroskill.respository.SysPermissionRespository;
import com.crimps.shiroskill.service.SysPermissionService;

import javax.annotation.Resource;

public class SysPermissionServiceImpl implements SysPermissionService {

    @Resource
    SysPermissionRespository sysPermissionRespository;

    /**
     * 添加权限
     *
     * @param permission 权限字符串
     * @param url        资源路径
     */
    @Override
    public void addSysPermission(String permission, String url) {

    }
}
