package com.magic.ssh.mapper;

import com.magic.ssh.entity.Task;
import javafx.util.Pair;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskMapper {

    Task queryTask(Integer taskId);


    Integer insertTask(Task task);

    Integer updateTask(Task task);

    Integer deleteTask(Integer taskId);

    List<Task> queryTaskByUserId(Integer userId);

    Task queryTaskByActions(String actionIds);

    Integer saveUser(List<Pair<Integer,String>> list);
}
