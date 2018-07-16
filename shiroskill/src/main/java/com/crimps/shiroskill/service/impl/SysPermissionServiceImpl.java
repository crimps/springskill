package com.crimps.shiroskill.service.impl;

import com.crimps.shiroskill.domain.entity.SysPermission;
import com.crimps.shiroskill.respository.SysPermissionRespository;
import com.crimps.shiroskill.service.SysPermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
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

    /**
     * 获取所有权限
     */
    @Override
    public List<SysPermission> getAllPermission() {
        return sysPermissionRespository.findAll();
    }
}
