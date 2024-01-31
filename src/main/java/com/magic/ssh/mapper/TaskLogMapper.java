package com.magic.ssh.mapper;

import com.magic.ssh.config.StatusConstants;
import com.magic.ssh.entity.Task;
import com.magic.ssh.entity.TaskLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskLogMapper {

    TaskLog queryTaskLog(Integer taskLogId);

    Integer insertTaskLog(TaskLog taskLog);

    List<TaskLog> queryLogsByIds(List<String> taskLogIds);

    List<TaskLog> queryLogsByUser(Integer userId);

    Integer updateTaskLog(TaskLog taskLog);


}
