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
    public Integer insertActionLog(ActionLog actionLog) {
        return actionLogMapper.insertActionLog(actionLog);
    }

    @Override
    public Integer deleteActionLogByIds(List<Integer> errLogIds) {
        return actionLogMapper.deleteActionLog(errLogIds);
    }

    @Override
    public List<ActionLog> getLogsByIds(List<Integer> actionLogIds) {
        return actionLogMapper.queryLogsByIds(actionLogIds);
    }

}
