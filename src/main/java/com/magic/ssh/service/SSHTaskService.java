package com.magic.ssh.service;

import com.magic.ssh.entity.Task;
import com.magic.ssh.entity.TaskLog;
import org.springframework.stereotype.Service;

@Service
public interface SSHTaskService {
    TaskLog execTask(Task task);
    TaskLog reExecTask(TaskLog taskLog);
}
