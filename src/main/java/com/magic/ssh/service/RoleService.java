package com.magic.ssh.service;

import com.magic.ssh.entity.security.SysRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {

    SysRole getRoleById(Integer roleId);

    SysRole insertRole(SysRole sysRole);

    SysRole updateRole(SysRole sysRole);

    boolean deleteRole(Integer roleId);

    List<SysRole> getAll();
}
