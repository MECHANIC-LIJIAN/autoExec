package com.magic.ssh.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TaskLogDetail {
    TaskLog taskLog;
    List<ActionLog> actionLogs;

    public TaskLogDetail(TaskLog taskLog, List<ActionLog> actionLogs) {
        this.taskLog = taskLog;
        this.actionLogs = actionLogs;
    }
}
