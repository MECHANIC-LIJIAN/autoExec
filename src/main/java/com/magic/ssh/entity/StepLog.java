package com.magic.ssh.entity;

import lombok.Data;

@Data
public class StepLog {
    Integer taskLogId;
    Integer actionLogId;
    Integer step;

    public StepLog(Integer taskLogId, Integer actionLogId, Integer step) {
        this.taskLogId = taskLogId;
        this.actionLogId = actionLogId;
        this.step = step;
    }
}
