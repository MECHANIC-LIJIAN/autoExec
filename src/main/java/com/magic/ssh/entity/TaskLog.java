package com.magic.ssh.entity;

import lombok.Data;

import java.util.List;

@Data
public class TaskLog {

    private Integer taskLogId;
    private Integer taskId;

    private Long startTime;
    private Long endTime;
    private Integer status;
    private String reason;
    private Integer curStep;

    List<ActionLog> actionLogs;
}
