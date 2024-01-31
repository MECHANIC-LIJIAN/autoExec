package com.magic.ssh.service.impl;

import com.magic.ssh.entity.ActionLog;
import com.magic.ssh.mapper.ActionLogMapper;
import com.magic.ssh.service.ActionLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ActionLogServiceImpl implements ActionLogService {
    
    
    private final ActionLogMapper actionLogMapper;

    public ActionLogServiceImpl(ActionLogMapper actionLogMapper) {
        this.actionLogMapper = actionLogMapper;
    }

    @Override
    public ActionLog getActionLogById(Integer actionLogId) {
        return actionLogMapper.queryActionLog(actionLogId);
    }

    @Override
    public List<ActionLog> getLogByTaskLogId(Integer taskLogId) {
        return actionLogMapper.queryLogByTaskLogId(taskLogId);
    }

    @Override
    public Integer insertActionLog(ActionLog actionLog) {
        return actionLogMapper.insertActionLog(actionLog);
    }

    @Override
    public Integer deleteActionLogByIds(Integer taskLogId, Integer step) {
        return actionLogMapper.deleteActionLog(taskLogId, step);
    }

    @Override
    public List<ActionLog> getActionLogByIds(List<Integer> actionLogIds) {
        return actionLogMapper.queryLogsByIds(actionLogIds);
    }

}
