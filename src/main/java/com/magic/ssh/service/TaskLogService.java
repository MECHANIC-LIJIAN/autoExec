package com.magic.ssh.service;

import com.magic.ssh.entity.TaskLog;
import com.magic.ssh.entity.StepLog;

import java.util.List;

public interface TaskLogService {
    TaskLog getTaskLogById(Integer taskLogId);

    TaskLog getTaskLogDetail(Integer taskLogId);

    Integer insertTaskLog(TaskLog taskLog);

    Integer insertStepLog(List<StepLog> stepLogs);

    List<StepLog> getTaskStepLogId(Integer taskLogId);

    Integer deleteStepLog(List<Integer> actionLogIds);

    Integer updateTaskLog(TaskLog taskLog);

    List<TaskLog> getTaskLogByUser(Integer userId);

}
