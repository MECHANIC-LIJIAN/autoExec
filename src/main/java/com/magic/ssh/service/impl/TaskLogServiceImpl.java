package com.magic.ssh.service.impl;

import com.magic.ssh.entity.TaskLog;
import com.magic.ssh.entity.StepLog;
import com.magic.ssh.mapper.TaskLogMapper;
import com.magic.ssh.service.TaskLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
public class TaskLogServiceImpl implements TaskLogService {
    private final TaskLogMapper taskLogMapper;

    public TaskLogServiceImpl(TaskLogMapper taskLogMapper) {
        this.taskLogMapper = taskLogMapper;
    }

    @Override
    public TaskLog getTaskLogById(Integer taskLogId) {
        return taskLogMapper.queryTaskLog(taskLogId);
    }

    @Override
    public TaskLog getTaskLogDetail(Integer taskLogId) {
        return taskLogMapper.queryLogByTaskLogId(taskLogId);
    }

    @Override
    public Integer insertTaskLog(TaskLog taskLog) {
        return taskLogMapper.insertTaskLog(taskLog);
    }

    @Override
    public Integer insertStepLog(List<StepLog> stepLogs) {
        return taskLogMapper.insertStepLog(stepLogs);
    }

    @Override
    public List<StepLog> getTaskStepLogId(Integer taskLogId) {
        return taskLogMapper.queryRelationList(taskLogId);
    }

    @Override
    public Integer deleteStepLog(List<Integer> actionLogIds) {
        return taskLogMapper.deleteStepLog(actionLogIds);
    }

    @Override
    public Integer updateTaskLog(TaskLog taskLog) {
        return taskLogMapper.updateTaskLog(taskLog);
    }

    @Override
    public List<TaskLog> getTaskLogByUser(Integer userId) {
        return taskLogMapper.queryLogsByUser(userId);
    }

}
