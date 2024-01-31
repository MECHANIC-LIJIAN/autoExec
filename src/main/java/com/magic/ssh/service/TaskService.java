package com.magic.ssh.service;
 

import com.magic.ssh.entity.Task;

import java.net.InetAddress;
import java.util.List;

public interface TaskService {
    Task getTaskInfoById(Integer taskId);

    Integer insertTask(Task task);

    Integer updateTask(Task task);

    Integer deleteTask(Integer taskId);

    List<Task> getTaskByUser(Integer userId);

    Integer saveUser(Integer userId,String taskIds);

}
