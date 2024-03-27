package com.magic.ssh.entity;

import javax.validation.constraints.*;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class Host {
    @NotBlank(groups = {Host.Update.class})
    private String hostId;

    @ExcelProperty("用户名")
    @NotBlank(groups = {Host.Update.class, Host.Create.class})
    private String username;

    @ExcelProperty("密码")
    @NotBlank(groups = {Host.Update.class, Host.Create.class})
    private String password;

    @ExcelProperty("ip地址")
    @NotBlank(groups = {Host.Update.class, Host.Create.class})
    private String ipAddress;

    @ExcelProperty("端口")
    @Min(value = 1, groups = {Host.Update.class, Host.Create.class})
    @Max(value = 65535, groups = {Host.Update.class, Host.Create.class})
    private Integer port;

    @ExcelProperty("主机名")
    private String hostname;

    private String passphrase;
    private String identity;
    private Integer status = 1;
    private String remark;

    // 更新分组
    public interface Update {
    }

    // 更新分组
    public interface Create {
    }

    public boolean importChk() {
        return StringUtils.hasText(hostname) && StringUtils.hasText(username) && StringUtils.hasText(
                password) && StringUtils.hasText(ipAddress) && StringUtils.hasText(String.valueOf(port));
    }
}