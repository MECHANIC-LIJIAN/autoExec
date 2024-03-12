package com.magic.ssh.controller;

import com.magic.ssh.entity.Action;
import com.magic.ssh.entity.Task;
import com.magic.ssh.service.ActionService;
import com.magic.ssh.service.TaskService;
import com.magic.ssh.util.Result;
import com.magic.ssh.util.ResultCode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/action")
public class ActionController {

    private final ActionService actionService;

    private final TaskService taskService;

    public ActionController(ActionService actionService, TaskService taskService) {
        this.actionService = actionService;
        this.taskService = taskService;
    }

    @GetMapping("/query")
    public Result guery(@RequestParam Integer actionId) {
        Action action = actionService.getActionInfoById(actionId);
        if (Objects.isNull(action)) {
            return Result.build(ResultCode.RESOURCE_NOT_EXIST);
        } else {
            return Result.success(action);
        }
    }

    @GetMapping("/queryByUser")
    public Result gueryRoles(@RequestParam Integer userId) {
        return  Result.success(actionService.getActionByUser(userId));
    }

    @PostMapping("/add")
    public Result add(@Validated(Action.Create.class) @RequestBody Action action) {

        if (actionService.insertAction(action) > 0) {
            return Result.success();
        } else {
            return Result.build(ResultCode.OP_ERROR);
        }

    }

    @PostMapping("/update")
    public Result update(@Validated(Action.Update.class) @RequestBody Action action) {
        if (actionService.updateAction(action) > 0) {
            return Result.success();
        } else {
            return Result.build(ResultCode.OP_ERROR);
        }
    }

    @PostMapping("/delete")
    public Result delete(@Validated(Action.Update.class) @RequestBody Action action) {

        Task task = taskService.getTaskByAction(action.getActionId());
        if (Objects.nonNull(task)) {
            return Result.build(ResultCode.OP_ERROR, "存在关联的任务 " + task.getTaskName() + ",无法删除该动作",
                    "");
        }

        if (actionService.deleteAction(action.getActionId()) > 0) {
            return Result.success();
        } else {
            return Result.build(ResultCode.OP_ERROR);
        }
    }

    @PostMapping("/saveUser")
    public Result saveUser(@RequestParam Integer userId, String roleId) {
        if (actionService.saveUser(userId, roleId) > 0) {
            return Result.success();
        } else {
            return Result.build(ResultCode.OP_ERROR);
        }
    }

}
