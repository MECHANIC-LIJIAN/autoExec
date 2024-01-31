package com.magic.ssh.entity;

import lombok.Data;

@Data
public class ExecLog {
    private Long startTime;
    private Long endTime;
    private String execRes;
    private Integer exitStatus;
}
