package com.crimps.shiroskill.respository;

import com.crimps.shiroskill.domain.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRespository extends JpaRepository<SysUser, Integer>, JpaSpecificationExecutor<SysUser> {

    /**
     * 根据用户名获取用户信息
     * @param userName 用户名
     * @return 用户信息
     */
    SysUser findSysUserByUsername(String userName);
}
