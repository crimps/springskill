package com.crimps.shiroskill.respository;

import com.crimps.shiroskill.domain.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SysPermissionRespository extends JpaRepository<SysPermission, String>, JpaSpecificationExecutor<SysPermission> {
}
