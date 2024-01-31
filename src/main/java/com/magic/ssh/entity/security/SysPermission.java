package com.magic.ssh.entity.security;

import lombok.Data;

@Data
public class SysPermission {
    private int id;
    //权限名称
    private String name;

    //权限描述
    private String descritpion;

    //授权链接
    private String url;

    //父节点id
    private int pid;
}
