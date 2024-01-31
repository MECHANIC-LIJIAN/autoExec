package com.magic.ssh.service.impl;

import com.magic.ssh.config.StatusConstants;
import com.magic.ssh.entity.*;
import com.magic.ssh.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SSHTaskServiceImpl implements SSHTaskService {

    private static final Integer firstStep = 0;

    private final TaskService taskService;

    private final TaskLogService taskLogService;

    private final SSHService sshService;

    private final ActionLogService actionLogService;

    public SSHTaskServiceImpl(TaskService taskService, TaskLogService taskLogService, SSHService sshService,
                              ActionLogService actionLogService) {
        this.taskService = taskService;
        this.taskLogService = taskLogService;
        this.sshService = sshService;
        this.actionLogService = actionLogService;
    }

    @Override
    public TaskLog execTask(Task task) {
        Task taskInfo = taskService.getTaskInfoById(task.getTaskId());
        if (Objects.isNull(taskInfo)) {
            return null;
        }
        List<List<Action>> actionList = taskInfo.getActionList();

        TaskLog taskLog = new TaskLog();
        taskLog.setTaskId(task.getTaskId());
        taskLog.setStartTime(System.currentTimeMillis());
        taskLog.setStatus(StatusConstants.TASK_READY);
        taskLog.setCurStep(firstStep);
        taskLogService.insertTaskLog(taskLog);
        log.debug(taskLog.toString());

        execActions(actionList, taskLog);

        return taskLog;
    }

    @Override
    public TaskLog reExecTask(TaskLog taskLog) {
        TaskLog queryLog = taskLogService.getTaskLogById(taskLog.getTaskLogId());
        if (Objects.isNull(queryLog)) {
            return null;
        }
        if (Objects.equals(queryLog.getStatus(), StatusConstants.TASK_DONE)) {
            taskLog.setReason("历史任务已完成");
            return taskLog;
        }

        Task task = taskService.getTaskInfoById(taskLog.getTaskId());
        if (Objects.isNull(task)) {
            taskLog.setReason("任务不存在");
            return taskLog;
        }

        List<List<Action>> actionList = task.getActionList();

        //从错误当前步开始取
        List<List<Action>> needExecList = actionList.subList(queryLog.getCurStep(), actionList.size());

        log.debug("任务的总步数 {}，出错的步：{}，重新执行步数 {}", actionList.size(), queryLog.getCurStep(),
                needExecList.size());

        //删除出错步的日志
        actionLogService.deleteActionLogByIds(queryLog.getTaskLogId(), queryLog.getCurStep());

        //执行出错步及以后的动作
        execActions(needExecList, queryLog);

        return queryLog;
    }

    private void execActions(List<List<Action>> actionList, TaskLog taskLog) {
        AtomicReference<Integer> taskStep = new AtomicReference<>(taskLog.getCurStep());

        CompletableFuture.runAsync(() -> {
            try {
                boolean breakFlag = false;
                ActionLog actionLog = null;

                for (List<Action> actions : actionList) {
                    //记录当前步数
                    taskLog.setCurStep(taskStep.get());

                    log.debug("任务的总步数 {}，当前步数 {}，执行动作数 {}", actionList.size(), taskStep.get(),
                            actions.size());
                    if (actions.size() == 1) {
                        //该步一个动作时，同步执行
                        actionLog = sshService.syncExecCmd(actions.get(0), taskLog.getTaskLogId(), taskStep.get());
                        //检查结果
                        breakFlag = checkBreak(actionLog, taskLog);
                    } else {
                        // 该步多个动作时，异步并行执行
                        List<ActionLog> actionLogs = new ArrayList<>();

                        //并行执行
                        List<CompletableFuture<ActionLog>> futureList = actions.stream()
                                .map(action -> sshService.asyncExecCmd(action, taskLog.getTaskLogId(), taskStep.get()))
                                .collect(Collectors.toList());

                        //收集执行结果
                        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                                futureList.toArray(new CompletableFuture[0])).whenComplete((v, t) -> {
                            // 所有CompletableFuture执行完成后进行遍历
                            futureList.forEach(future -> {
                                synchronized (this) {
                                    // 查询结果合并
                                    actionLogs.add(future.getNow(null));
                                }
                            });
                        });

                        allFutures.get();
                        for (ActionLog actionLog1 : actionLogs) {
                            actionLog = actionLog1;
                            //检查结果
                            breakFlag = checkBreak(actionLog1, taskLog);
                            if (breakFlag) {
                                break;
                            }
                        }
                    }
                    //遇到错误中断任务
                    if (breakFlag) {
                        break;
                    }
                    taskStep.getAndSet(taskStep.get() + 1);
                }

                //记录任务状态
                if (breakFlag) {
                    //有错误
                    taskLog.setStatus(StatusConstants.TASK_ERROR);
                    taskLog.setReason(actionLog.getReason());
                    log.error(taskLog.getReason());
                } else {
                    //全部执行
                    taskLog.setStatus(StatusConstants.TASK_DONE);
                    taskLog.setReason("");
                }
            } catch (ExecutionException | InterruptedException e) {
                log.error(e.getMessage());
                taskLog.setStatus(StatusConstants.TASK_ERROR);
                taskLog.setReason("内部错误");
            } finally {
                taskLog.setEndTime(System.currentTimeMillis());
                taskLogService.updateTaskLog(taskLog);
                log.debug(taskLog.toString());
            }
        });
    }

    private Boolean checkBreak(ActionLog actionLog, TaskLog taskLog) {
        if (!Objects.equals(actionLog.getStatus(), StatusConstants.ACTION_DONE)) {
            taskLog.setStatus(StatusConstants.TASK_ERROR);
            taskLog.setReason(actionLog.getReason());
            return true;
        }
        return false;
    }

}

