package com.magic.ssh.entity.security;


import lombok.Data;

@Data
public class SysUser{
    private Integer userId;
    private String username;
    private String password;
    private String salt;
    private String desc;
}
