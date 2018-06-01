package com.crimps.shiroskill.service;

import com.crimps.shiroskill.domain.entity.SysUser;

public interface SysUserService {

    /**
     * 根据用户名获取用户信息
     * @param userName 用户名
     * @return 用户信息
     */
    SysUser findByUsername(String userName);
}
