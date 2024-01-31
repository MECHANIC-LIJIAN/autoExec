package com.magic.ssh.service;
 

import com.magic.ssh.entity.Task;
import com.magic.ssh.entity.TaskLog;
import com.magic.ssh.entity.TaskLogDetail;

import java.util.List;

public interface TaskLogService {
    TaskLog getTaskLogById(Integer taskLogId);

    TaskLogDetail getTaskLogDetail(Integer taskLogId);

    Integer insertTaskLog(TaskLog taskLog);

    Integer updateTaskLog(TaskLog taskLog);

    List<TaskLog> getTaskLogByUser(Integer userId);

}
