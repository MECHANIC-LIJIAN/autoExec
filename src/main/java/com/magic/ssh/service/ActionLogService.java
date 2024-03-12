package com.magic.ssh.service;
 

import com.magic.ssh.entity.ActionLog;

import java.util.List;

public interface ActionLogService {
    ActionLog getActionLogById(Integer actionLogId);

    Integer insertActionLog(ActionLog actionLog);

    Integer deleteActionLogByIds(List<Integer> errLogIds);

    List<ActionLog> getLogsByIds(List<Integer> actionLogIds);
}
