package com.magic.ssh.service;

import com.magic.ssh.entity.Action;
import com.magic.ssh.entity.TaskLog;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskExecService {

    @Async("taskExecutor")
    void asyncExec(List<List<Action>> stepList, TaskLog taskLog);
}
