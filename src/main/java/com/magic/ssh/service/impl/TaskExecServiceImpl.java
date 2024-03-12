package com.magic.ssh.service.impl;

import com.magic.ssh.config.StatusConstants;
import com.magic.ssh.entity.Action;
import com.magic.ssh.entity.ActionLog;
import com.magic.ssh.entity.StepLog;
import com.magic.ssh.entity.TaskLog;
import com.magic.ssh.service.SSHService;
import com.magic.ssh.service.TaskExecService;
import com.magic.ssh.service.TaskLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TaskExecServiceImpl implements TaskExecService {

    private final SSHService sshService;

    private final TaskLogService taskLogService;

    public TaskExecServiceImpl(SSHService sshService, TaskLogService taskLogService) {
        this.sshService = sshService;
        this.taskLogService = taskLogService;
    }

    @Override
    public void asyncExec(List<List<Action>> stepList, TaskLog taskLog) {
        log.debug("exec actions, step size: {}", stepList.size());
        AtomicReference<Integer> taskStep = new AtomicReference<>(taskLog.getCurStep());
        List<StepLog> taskStepActionList = new ArrayList<>();
        CompletableFuture.runAsync(() -> {
            try {
                boolean breakFlag = false;
                ActionLog actionLog = null;

                for (List<Action> actions : stepList) {
                    //记录当前步数
                    taskLog.setCurStep(taskStep.get());

                    log.debug("任务的总步数 {}，当前步数 {}，执行动作数 {}", stepList.size(), taskStep.get(),
                            actions.size());
                    if (actions.size() == 1) {
                        //该步一个动作时，同步执行
                        actionLog = sshService.syncExecCmd(actions.get(0));
                        taskStepActionList.add(new StepLog(taskLog.getTaskLogId(), actionLog.getActionLogId(),
                                taskStep.get()));
                        //检查结果
                        breakFlag = checkBreak(actionLog, taskLog);
                    } else {
                        // 该步多个动作时，异步并行执行
                        List<ActionLog> actionLogs = new ArrayList<>();
                        //并行执行
                        List<CompletableFuture<ActionLog>> futureList = actions.stream()
                                .map(sshService::asyncExecCmd)
                                .collect(Collectors.toList());
                        //收集执行结果
                        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                                futureList.toArray(new CompletableFuture[0])).whenComplete((v, t) -> {
                            // 所有CompletableFuture执行完成后进行遍历 查询结果合并
                            futureList.forEach(future -> {
                                synchronized (this) {
                                    actionLogs.add(future.getNow(null));
                                }
                            });
                        });

                        allFutures.get();
                        for (ActionLog actionLog1 : actionLogs) {
                            taskStepActionList.add(new StepLog(taskLog.getTaskLogId(), actionLog1.getActionLogId(),
                                    taskStep.get()));
                            actionLog = actionLog1;
                            //检查结果
                            breakFlag = checkBreak(actionLog1, taskLog);
                            if (breakFlag) {
                                break;
                            }
                        }
                    }

                    // 记录动作日志
                    taskLogService.insertStepLog(taskStepActionList);
                    taskStepActionList.clear();
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
                    log.error("任务中断,原因是: {}", taskLog.getReason());
                } else {
                    //全部执行
                    taskLog.setStatus(StatusConstants.TASK_DONE);
                    taskLog.setReason("");
                }
                log.debug("任务执行完毕！");

            } catch (ExecutionException | InterruptedException e) {
                log.error(e.getMessage());
                taskLog.setStatus(StatusConstants.TASK_ERROR);
                taskLog.setReason("内部错误");
            } finally {

                log.debug("记录任务日志...");
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
