package com.crimps.shiroskill.service.impl;

import com.crimps.shiroskill.domain.entity.SysUser;
import com.crimps.shiroskill.respository.SysUserRespository;
import com.crimps.shiroskill.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    SysUserRespository sysUserRespository;

    /** 根据用户名获取用户信息
     * @param userName 用户名
     * @return 用户信息
     */
    @Override
    public SysUser findByUsername(String userName) {
        return sysUserRespository.findSysUserByUsername(userName);
    }
}
