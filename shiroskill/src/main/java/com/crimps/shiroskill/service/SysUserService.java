package com.crimps.shiroskill.service;

import com.crimps.shiroskill.domain.entity.SysUser;

public interface SysUserService {

    /**
     *
     * @param userName
     * @return
     */
    SysUser findByUsername(String userName);
}
