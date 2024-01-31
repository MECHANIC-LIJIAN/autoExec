package com.magic.ssh.entity.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class SysRole implements GrantedAuthority {
    private Integer roleId;
    private String roleName;

    @Override
    public String getAuthority() {
        return roleName;
    }
}
