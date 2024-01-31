package com.magic.ssh.controller;

import com.magic.ssh.config.StatusConstants;
import com.magic.ssh.entity.*;
import com.magic.ssh.service.*;
import com.magic.ssh.util.Result;
import com.magic.ssh.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api")
public class SSHController {

    private final SSHService sshService;

    private final SSHTaskService sshTaskService;

    private final TaskLogService taskLogService;

    public SSHController(SSHService sshService, SSHTaskService sshTaskService, TaskLogService taskLogService) {
        this.sshService = sshService;
        this.sshTaskService = sshTaskService;
        this.taskLogService = taskLogService;
    }

    @PostMapping("/init")
    public Result init(@RequestBody Host host) {
        String[] hostIds = host.getHostId().split(",");
        HashMap<String, Result> map = new HashMap<>();
        Result result = null;
        for (String hostId : hostIds) {
            host.setHostId(hostId);
            log.info(host.toString());
            if (sshService.syncConnect(host)){
                result = Result.success();
            }else{
                result = Result.build(ResultCode.JSCH_CONNECT_ERROR);
            }
            map.put(host.getHostId(), result);
        }
        return Result.success(map);
    }

    @PostMapping("/initBatch")
    public Result initBatch(@RequestBody List<Host> hosts) {
        HashMap<String, Result> map = new HashMap<>();
        Result result = null;
        for (Host host : hosts) {
            log.info(host.toString());
            if (sshService.syncConnect(host)){
                result = Result.success();
            }else{
                result = Result.build(ResultCode.JSCH_CONNECT_ERROR);
            }
            map.put(host.getHostId(), result);
        }
        return Result.success(map);
    }

    @PostMapping("/exec")
    public Result exec(@Validated(Action.Exec.class) @RequestBody Action action) {
        action.setCmd("");
        action.setHostId("");
        ActionLog actionLog = sshService.syncExecCmd(action);

        if (Objects.equals(actionLog.getStatus(), StatusConstants.ACTION_DONE)){
            return Result.success(actionLog);
        }else {
            return Result.build(ResultCode.TASK_EXEC_ERROR,"",actionLog);
        }
    }

    @PostMapping("/execTask")
    public Result execTask(@Validated(Task.Exec.class) @RequestBody Task task) {
        TaskLog taskLog = sshTaskService.execTask(task);
        if (Objects.isNull(taskLog)){
            return Result.build(ResultCode.TASK_NOT_EXIST);
        }else {
            return Result.success(taskLog);
        }
    }

    @PostMapping("/reExecTask")
    public Result reExecTask(@Validated @RequestBody TaskLog taskLog) {
        TaskLog reExecTaskLog = sshTaskService.reExecTask(taskLog);
        if (Objects.isNull(taskLog)){
            return Result.build(ResultCode.OP_ERROR,"任务历史不存在");
        }else {
            return Result.success(reExecTaskLog);
        }
    }


    @GetMapping("/taskLog")
    public Result taskLog(@RequestParam Integer taskLogId) {
        return Result.success(taskLogService.getTaskLogDetail(taskLogId));
    }
}

