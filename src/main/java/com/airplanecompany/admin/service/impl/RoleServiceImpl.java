package com.airplanecompany.admin.service.impl;

import com.airplanecompany.admin.dao.RoleDao;
import com.airplanecompany.admin.entity.Role;
import com.airplanecompany.admin.service.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role createRole(String roleName) {
        return roleDao.save(new Role(roleName));
    }
}
