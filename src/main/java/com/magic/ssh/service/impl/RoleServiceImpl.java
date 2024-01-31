package com.magic.ssh.service.impl;

import com.magic.ssh.entity.security.SysRole;
import com.magic.ssh.mapper.SysRoleMapper;
import com.magic.ssh.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
public class RoleServiceImpl implements RoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public SysRole getRoleById(Integer roleId) {
        return sysRoleMapper.getRoleById(roleId);
    }

    @Override
    @Transactional
    public SysRole insertRole(SysRole sysRole) {
        if (sysRoleMapper.insertRole(sysRole) > 0) {
            return sysRole;
        }
        return null;
    }

    @Override
    public SysRole updateRole(SysRole sysRole) {
        log.info(sysRole.toString());

        if (sysRoleMapper.updateRole(sysRole) > 0) {
            return sysRole;
        }
        return null;
    }

    @Override
    public boolean deleteRole(Integer roleId) {
        log.info(String.valueOf(roleId));

        if (sysRoleMapper.deleteRole(roleId) > 0) {
            return true;
        }

        return false;
    }

    @Override
    public List<SysRole> getAll() {
        return sysRoleMapper.getAllRole();
    }
}
