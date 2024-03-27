package com.magic.ssh.entity;

import lombok.Data;

import java.util.List;

@Data
public class TaskLog {

    private Integer taskLogId;
    private Integer taskId;
    private String taskName;

    private Long startTime;
    private Long endTime;
    private Long cost;

    private Integer status;
    private String reason;
    private Integer curStep;

    List<ActionLog> actionLogs;
}
