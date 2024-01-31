package com.magic.ssh.mapper;

import com.magic.ssh.entity.security.SysPermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysPermissionMapper {
    List<SysPermission> getAllPermission();
    List<SysPermission> getPermissionsByUserName(String username);
}
