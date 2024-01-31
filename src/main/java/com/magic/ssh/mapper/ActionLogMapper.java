package com.magic.ssh.mapper;

import com.magic.ssh.entity.ActionLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ActionLogMapper {

    ActionLog queryActionLog(Integer actionLogId);

    Integer insertActionLog(ActionLog actionLog);

    Integer deleteActionLog(Integer taskLogId, Integer step);

    List<ActionLog> queryLogByTaskLogId(Integer taskLogId);

    List<ActionLog> queryLogsByIds(List<Integer> actionLogIds);

}
