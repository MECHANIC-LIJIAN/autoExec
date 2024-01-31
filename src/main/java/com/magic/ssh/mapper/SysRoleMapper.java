package com.magic.ssh.mapper;


import com.magic.ssh.entity.security.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SysRoleMapper {

    List<SysRole> getAllRole();

    SysRole getRoleById(Integer roleId);

    SysRole getRoleByName(String roleName);

    Integer insertRole(SysRole sysRole);

    Integer updateRole(SysRole sysRole);

    Integer deleteRole(Integer roleId);


}
