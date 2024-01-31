package com.magic.ssh.service.impl;

import com.magic.ssh.entity.security.JwtUser;
import com.magic.ssh.entity.security.SysPermission;
import com.magic.ssh.entity.security.SysRole;
import com.magic.ssh.entity.security.SysUser;
import com.magic.ssh.mapper.SysPermissionMapper;
import com.magic.ssh.mapper.SysUserMapper;
import com.magic.ssh.service.UserService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public SysUser getUserInfoByUserName(String username) {
        return sysUserMapper.getUser(username);
    }

    @Override
    public Integer insertUser(SysUser sysUser) {
        return sysUserMapper.insertUser(sysUser);
    }

    @Override
    public Integer updateUser(SysUser sysUser) {
        return sysUserMapper.updateUser(sysUser);
    }

    @Override
    public Integer deleteUser(String username) {
        return sysUserMapper.deleteUser(username);
    }

    @Override
    public JwtUser getUserDetailInfoByUserName(String username) {
        return sysUserMapper.getUserInfo(username);
    }

    @Override
    public List<SysRole> getUserRoleByUserName(String username) {
        return sysUserMapper.getRoleByUserName(username);
    }

    @Override
    public List<SysPermission> getUserPermissionsByUserName(String username) {
        return sysPermissionMapper.getPermissionsByUserName(username);
    }

    @Override
    public Integer saveRole(Integer userId, String roleIds) {
        String[] roleList=roleIds.split(",");
        
        List<Pair<Integer,String>> list=new ArrayList<>();
        for (String role:roleList) {
            list.add(new Pair<>(userId,role));
        }

        return sysUserMapper.saveRole(list);
    }
}
