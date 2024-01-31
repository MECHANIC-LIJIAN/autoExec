package com.magic.ssh.service;

import com.magic.ssh.entity.Action;
import com.magic.ssh.entity.ActionLog;
import com.magic.ssh.entity.Host;
import com.magic.ssh.entity.TaskLog;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public interface SSHService {

    Boolean syncConnect(Host host);

    void close(String host);

    ActionLog syncExecCmd(Action action, Integer taskLogId,Integer taskStep);

    @Async("execExecutor")
    CompletableFuture<ActionLog> asyncExecCmd(Action action, Integer taskLogId,Integer taskStep);

    ActionLog syncExecCmd(Action action);

    @Async("execExecutor")
    CompletableFuture<ActionLog> asyncExecCmd(Action action);


}
