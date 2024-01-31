package com.magic.ssh.service.impl;

import com.magic.ssh.entity.ActionLog;
import com.magic.ssh.entity.TaskLog;
import com.magic.ssh.entity.TaskLogDetail;
import com.magic.ssh.mapper.TaskLogMapper;
import com.magic.ssh.service.ActionLogService;
import com.magic.ssh.service.TaskLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskLogServiceImpl implements TaskLogService {
    private final TaskLogMapper taskLogMapper;

    private final ActionLogService actionLogService;

    public TaskLogServiceImpl(TaskLogMapper taskLogMapper, ActionLogService actionLogService) {
        this.taskLogMapper = taskLogMapper;
        this.actionLogService = actionLogService;
    }

    @Override
    public TaskLog getTaskLogById(Integer taskLogId) {
        return taskLogMapper.queryTaskLog(taskLogId);
    }

    @Override
    public TaskLogDetail getTaskLogDetail(Integer taskLogId) {
        TaskLog taskLog = taskLogMapper.queryTaskLog(taskLogId);
        if (Objects.isNull(taskLog)) {
            return null;
        }
        List<ActionLog> actionLogs = actionLogService.getLogByTaskLogId(taskLogId);

//        List<List<ActionLog>> sortedList = new ArrayList<>();
//        actionLogs.stream().collect(Collectors.groupingBy(ActionLog::getStep)).forEach(sortedList::add);
        return new TaskLogDetail(taskLog, actionLogs);
    }

    @Override
    public Integer insertTaskLog(TaskLog taskLog) {
        return taskLogMapper.insertTaskLog(taskLog);
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
