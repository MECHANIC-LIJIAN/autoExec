package com.magic.ssh.mapper;

import com.magic.ssh.entity.StepAction;
import com.magic.ssh.entity.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskMapper {

    Task queryTask(Integer taskId);

    Task queryTaskInfo(Integer taskId);

    Integer insertTask(Task task);


    Integer updateTask(Task task);

    Integer deleteTask(Integer taskId);

    List<Task> queryTaskByUserId(Integer userId);

    Task queryTaskByAction(Integer actionId);

    Integer insertStep(List<StepAction> stepActions);

    List<StepAction> queryStep(Integer taskId);

    Integer deleteStep(Integer taskId);

}
