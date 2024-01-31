package com.magic.ssh.service.impl;

import com.magic.ssh.entity.Action;
import com.magic.ssh.entity.Task;
import com.magic.ssh.mapper.ActionMapper;
import com.magic.ssh.mapper.TaskMapper;
import com.magic.ssh.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;

    private final ActionMapper actionMapper;

    public TaskServiceImpl(TaskMapper taskMapper, ActionMapper actionMapper) {
        this.taskMapper = taskMapper;
        this.actionMapper = actionMapper;
    }

    @Override
    public Integer insertTask(Task task) {
        if (Objects.nonNull(taskMapper.queryTaskByActions(task.getActionIds()))) {
            return 0;
        }
        List<Integer> actionIds = getActionIds(task.getActionIds());
        if (actionMapper.queryNumByIds(actionIds) != actionIds.size()) {
            return 0;
        }
        return taskMapper.insertTask(task);
    }

    @Override
    public Integer updateTask(Task task) {
        Task myTask = taskMapper.queryTask(task.getTaskId());
        if (Objects.isNull(myTask)) {
            return -1;
        }
        List<Integer> actionIds = getActionIds(task.getActionIds());
        if (actionMapper.queryNumByIds(actionIds) != actionIds.size()) {
            return 0;
        }
        return taskMapper.updateTask(task);
    }

    @Override
    public Integer deleteTask(Integer taskId) {
        Task myTask = taskMapper.queryTask(taskId);
        if (Objects.isNull(myTask)) {
            return -1;
        }
        return taskMapper.deleteTask(taskId);
    }

    @Override
    public List<Task> getTaskByUser(Integer useId) {
        return taskMapper.queryTaskByUserId(useId);
    }

    @Override
    public Integer saveUser(Integer userId, String taskIds) {
        return null;
    }



    @Override
    public Task getTaskInfoById(Integer taskId) {
        Task task = taskMapper.queryTask(taskId);
        if (Objects.isNull(task)) {
            return null;
        }
//        log.debug(task.toString());

        List<Integer> actionIds = getActionIds(task.getActionIds());
        List<Action> actionList = actionMapper.queryActionsByIds(actionIds);
        List<List<Action>> execList = new ArrayList<>();
        for (List<Integer> ids : getActionArrays(task.getActionIds())) {
            execList.add(actionList.stream().filter(action -> {
                return ids.contains(action.getActionId());
            }).collect(Collectors.toList()));
//            log.debug(Arrays.toString(execList.get(execList.size()-1).toArray()));
        }

        task.setActionList(execList);
        return task;
    }


    private List<List<Integer>> getActionArrays(String actionIds) {
        String[] steps = actionIds.split("#");
        List<List<Integer>> actionList = new ArrayList<>();
        for (String s : steps) {
            actionList.add(Arrays.stream(s.split(",")).map(Integer::valueOf).collect(Collectors.toList()));
        }
        return actionList;
    }

    private List<Integer> getActionIds(String actionIds) {
        return getActionArrays(actionIds).stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    private List<List<Action>> getExecActionList(Task task){
        List<Integer> actionIds = getActionIds(task.getActionIds());
        List<Action> actionList = actionMapper.queryActionsByIds(actionIds);
        List<List<Action>> execList = new ArrayList<>();
        for (List<Integer> ids : getActionArrays(task.getActionIds())) {
            execList.add(actionList.stream().filter(action -> {
                return ids.contains(action.getActionId());
            }).collect(Collectors.toList()));
            log.debug(Arrays.toString(execList.get(execList.size()-1).toArray()));
        }
        return execList;
    }
}
