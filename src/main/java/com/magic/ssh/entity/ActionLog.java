package com.magic.ssh.entity;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ActionLog {
    private Integer actionLogId;
    private Integer taskLogId;
    private Integer step;
    private Integer actionId;
    private Long startTime;
    private Long endTime;
    private String execRes;
    private Integer exitStatus;
    private Integer status;
    private String reason;
    private Integer checkRes;

    private String hostName;
    private String actionName;
}
