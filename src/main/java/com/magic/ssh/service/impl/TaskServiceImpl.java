package com.magic.ssh.service.impl;

import com.magic.ssh.SysConstants;
import com.magic.ssh.entity.Action;
import com.magic.ssh.entity.StepAction;
import com.magic.ssh.entity.Task;
import com.magic.ssh.mapper.ActionMapper;
import com.magic.ssh.mapper.TaskMapper;
import com.magic.ssh.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Task getTask(Integer taskId) {
        return taskMapper.queryTask(taskId);
    }

    @Transactional
    @Override
    public Integer insertTask(Task task) {

        Task task1 = taskMapper.queryTaskByName(task.getTaskName());
        if (Objects.nonNull(task1)){
            return SysConstants.RESOURCE_DP;
        }

        List<StepAction> stepActions = task.getStepData();

        if (chkActions(stepActions)) {
            log.error("存在无效动作");
            return SysConstants.OP_ERROR;
        }

        if (taskMapper.insertTask(task) == 0) {
            log.error("任务添加失败");
            return SysConstants.OP_ERROR;
        }

        stepActions.forEach(o -> o.setTaskId(task.getTaskId()));
        if (taskMapper.insertStep(stepActions) == 0) {
            log.error("任务关联动作失败");
            return SysConstants.OP_ERROR;
        }
        return SysConstants.OP_SUCCESS;
    }

    @Override
    public Integer updateTask(Task task) {
        Task myTask = taskMapper.queryTask(task.getTaskId());
        if (Objects.isNull(myTask)) {
            return SysConstants.OP_ERROR;
        }

        List<StepAction> stepActions = task.getStepData();

        if (chkActions(stepActions)) {
            log.error("存在无效动作");
            return SysConstants.OP_ERROR;
        }

        if (taskMapper.deleteStep(task.getTaskId()) == 0) {
            return SysConstants.OP_ERROR;
        }

        if (taskMapper.insertStep(stepActions) == 0) {
            return SysConstants.OP_ERROR;
        }

        return taskMapper.updateTask(task);
    }

    private boolean chkActions(List<StepAction> stepActions) {
        List<Integer> actionIds = stepActions.stream().map(StepAction::getActionId).collect(Collectors.toList());
        List<Action> actionList = actionMapper.queryActionsByIds(actionIds);
        return !Objects.isNull(actionList) &&
                actionList.stream().map(Action::getActionId).collect(Collectors.toList()).contains(actionIds);
    }

    @Override
    public Integer deleteTask(Integer taskId) {
        Task myTask = taskMapper.queryTask(taskId);
        if (Objects.isNull(myTask)) {
            return SysConstants.OP_ERROR;
        }
        taskMapper.deleteStep(taskId);
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
        return taskMapper.queryTaskInfo(taskId);
    }

    @Override
    public Task getTaskByAction(Integer actionId) {
        return taskMapper.queryTaskByAction(actionId);
    }

    @Override
    public List<List<Action>> getExecStepList(Task taskInfo) {
//        log.debug(Arrays.toString(taskInfo.getActionList().toArray()));
         return taskInfo.getActionList().stream().collect(Collectors.groupingBy(Action::getStep))
                        .entrySet().stream().sorted(Map.Entry.comparingByKey()).map(Map.Entry::getValue)
                        .collect(Collectors.toList());

    }
//    private List<List<Integer>> getActionArrays(String actionIds) {
//        String[] steps = actionIds.split(SysConstants.STEP_DELI);
//        List<List<Integer>> actionList = new ArrayList<>();
//        for (String s : steps) {
//            actionList.add(Arrays.stream(s.split(SysConstants.ACTION_DELI)).map(Integer::valueOf)
//                    .collect(Collectors.toList()));
//        }
//        return actionList;
//    }
//
//    private List<Integer> getActionIds(String actionIdStr) {
//        return Arrays.stream(actionIdStr.split("[,#]"))
//                .map(Integer::parseInt).collect(
//                        Collectors.toList());
//    }
//
//    private List<List<Action>> getExecActionList(Task task) {
//        List<Integer> actionIds = getActionIds(task.getActionIdStr());
//        List<Action> actionList = actionMapper.queryActionsByIds(actionIds);
//        List<List<Action>> execList = new ArrayList<>();
//        for (List<Integer> ids : getActionArrays(task.getActionIdStr())) {
//            execList.add(actionList.stream().filter(action -> {
//                return ids.contains(action.getActionId());
//            }).collect(Collectors.toList()));
//            log.debug(Arrays.toString(execList.get(execList.size() - 1).toArray()));
//        }
//        return execList;
//    }
}
