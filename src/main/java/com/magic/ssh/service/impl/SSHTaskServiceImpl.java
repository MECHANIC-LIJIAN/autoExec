package com.magic.ssh.service.impl;

import com.magic.ssh.config.StatusConstants;
import com.magic.ssh.entity.*;
import com.magic.ssh.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SSHTaskServiceImpl implements SSHTaskService {

    private static final Integer firstStep = 0;

    private final TaskService taskService;

    private final TaskLogService taskLogService;

    private final TaskExecService taskExecService;

    private final ActionLogService actionLogService;

    public SSHTaskServiceImpl(TaskService taskService, TaskLogService taskLogService, TaskExecService taskExecService,
                              ActionLogService actionLogService) {
        this.taskService = taskService;
        this.taskLogService = taskLogService;
        this.taskExecService = taskExecService;
        this.actionLogService = actionLogService;
    }

    @Transactional
    @Override
    public TaskLog execTask(Task task) {
        Task taskInfo = taskService.getTaskInfoById(task.getTaskId());
        if (Objects.isNull(taskInfo)) {
            return null;
        }

        List<List<Action>> execStepList = taskService.getExecStepList(taskInfo);

        TaskLog taskLog = new TaskLog();
        taskLog.setTaskId(task.getTaskId());
        taskLog.setStartTime(System.currentTimeMillis());
        taskLog.setStatus(StatusConstants.TASK_READY);
        taskLog.setCurStep(firstStep);

        taskLogService.insertTaskLog(taskLog);

        taskExecService.asyncExec(execStepList, taskLog);

        return taskLog;
    }

    @Override
    public TaskLog reExecTask(TaskLog taskLog) {
        TaskLog queryLog = taskLogService.getTaskLogDetail(taskLog.getTaskLogId());
        if (Objects.isNull(queryLog)) {
            return null;
        }
        if (Objects.equals(queryLog.getStatus(), StatusConstants.TASK_DONE)) {
            queryLog.setReason("历史任务已完成");
            return queryLog;
        }

        Task taskInfo = taskService.getTaskInfoById(queryLog.getTaskId());
        if (Objects.isNull(taskInfo)) {
            queryLog.setReason("任务不存在");
            return queryLog;
        }

        List<List<Action>> execStepList = taskService.getExecStepList(taskInfo);

        //从错误当前步开始取剩余动作
        List<List<Action>> needExecList = execStepList.subList(queryLog.getCurStep(), execStepList.size());

        log.debug("任务的总步数 {}，出错的步：{}，重新执行步数 {}", execStepList.size(), queryLog.getCurStep(),
                needExecList.size());

        log.debug(queryLog.toString());
        List<ActionLog> stepActionLogs = queryLog.getActionLogs();

        List<Integer> needDel =
                stepActionLogs.stream().filter(o -> o.getStep().equals(queryLog.getCurStep()))
                        .map(ActionLog::getActionLogId).collect(Collectors.toList());

        //删除出错步的日志
        actionLogService.deleteActionLogByIds(needDel);
        taskLogService.deleteStepLog(needDel);
        //执行出错步及以后的动作
        taskExecService.asyncExec(needExecList, queryLog);
        return queryLog;
    }


}

