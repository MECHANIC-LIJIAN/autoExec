package com.magic.ssh.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TaskLog {

    @NotNull
    private Integer taskLogId;
    @NotNull
    private Integer taskId;

    private Long startTime;
    private Long endTime;
    private Integer status;
    private String reason;
    private Integer curStep;
}
