package com.magic.ssh.mapper;


import com.magic.ssh.entity.security.JwtUser;
import com.magic.ssh.entity.security.SysRole;
import com.magic.ssh.entity.security.SysUser;
import javafx.util.Pair;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserMapper {

    SysUser getUser(String username);

    Integer insertUser(SysUser sysUser);

    Integer updateUser(SysUser sysUser);

    Integer deleteUser(String username);

    JwtUser getUserInfo(String username);

    List<SysRole> getRoleByUserId(Integer userId);

    List<SysRole> getRoleByUserName(String username);

    Integer saveRole(List<Pair<Integer,String>> list);
}
