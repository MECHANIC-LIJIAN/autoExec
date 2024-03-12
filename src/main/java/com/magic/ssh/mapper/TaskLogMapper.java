package com.magic.ssh.mapper;

import com.magic.ssh.entity.TaskLog;
import com.magic.ssh.entity.StepLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskLogMapper {

    TaskLog queryTaskLog(Integer taskLogId);

    Integer insertTaskLog(TaskLog taskLog);

    Integer updateTaskLog(TaskLog taskLog);

    List<TaskLog> queryLogsByIds(List<String> taskLogIds);

    List<TaskLog> queryLogsByUser(Integer userId);

    Integer insertStepLog(List<StepLog> stepLogs);

    Integer deleteStepLog(List<Integer> actionLogIds);

    List<StepLog> queryRelationList(Integer taskLogId);

    TaskLog queryLogByTaskLogId(Integer taskLogId);

}
