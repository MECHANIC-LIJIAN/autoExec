package com.magic.ssh.service;
 

import com.magic.ssh.entity.ActionLog;

import java.util.List;

public interface ActionLogService {
    ActionLog getActionLogById(Integer actionLogId);

    List<ActionLog> getLogByTaskLogId(Integer taskLogId);

    Integer insertActionLog(ActionLog actionLog);

    Integer deleteActionLogByIds(Integer taskLogId, Integer step);


    List<ActionLog> getActionLogByIds(List<Integer> actionLogIds);
}
