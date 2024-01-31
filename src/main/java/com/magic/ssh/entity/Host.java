package com.magic.ssh.entity;

import javax.validation.constraints.*;

import lombok.Data;

@Data
public class Host {
    @NotBlank(groups = {Host.Update.class})
    private String hostId;

    @NotBlank(groups = {Host.Update.class, Host.Create.class})
    private String username;

    @NotBlank(groups = {Host.Update.class, Host.Create.class})
    private String ipAddress;

    @Min(value = 1, groups = {Host.Update.class, Host.Create.class})
    @Max(value = 65535, groups = {Host.Update.class, Host.Create.class})
    private Integer port;
    
    private String hostname;

    @NotBlank(groups = {Host.Update.class, Host.Create.class})
    private String password;

    private String passphrase;
    private String identity;
    private Integer status = 1;
    private String remark;

    // 更新分组
    public interface Update {}
    // 更新分组
    public interface Create {}
}