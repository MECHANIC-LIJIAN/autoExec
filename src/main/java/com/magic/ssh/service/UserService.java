package com.magic.ssh.service;

import com.magic.ssh.entity.security.JwtUser;
import com.magic.ssh.entity.security.SysPermission;
import com.magic.ssh.entity.security.SysRole;
import com.magic.ssh.entity.security.SysUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    SysUser getUserInfoByUserName(String username);

    Integer insertUser(SysUser sysUser);

    Integer updateUser(SysUser sysUser);

    Integer deleteUser(String username);

    JwtUser getUserDetailInfoByUserName(String username);

    List<SysRole> getUserRoleByUserName(String username);

    List<SysPermission> getUserPermissionsByUserName(String username);

    Integer saveRole(Integer userId,String roleIds);
}
