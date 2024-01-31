package com.magic.ssh.controller;

import com.magic.ssh.entity.Task;
import com.magic.ssh.service.TaskService;
import com.magic.ssh.util.Result;
import com.magic.ssh.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/query")
    public Result guery(@RequestParam Integer taskId) {
        Task task = taskService.getTaskInfoById(taskId);
        if (Objects.isNull(task)) {
            return Result.build(ResultCode.OP_ERROR);
        } else {
            return Result.success( task);
        }
    }

    @GetMapping("/queryByUser")
    public Result gueryRoles(@RequestParam Integer userId) {

        List<Task> list = taskService.getTaskByUser(userId);
        if (list.isEmpty()) {
            return Result.build(ResultCode.OP_ERROR);
        } else {
            return Result.success( list);
        }
    }

    @PostMapping("/add")
    public Result add(@Validated(Task.Create.class) @RequestBody Task task) {
        if (taskService.insertTask(task) > 0) {
            return Result.success();
        } else {
            return Result.build(ResultCode.OP_ERROR);
        }

    }

    @PostMapping("/update")
    public Result update(@Validated(Task.Update.class) @RequestBody Task task) {
        if (taskService.updateTask(task) > 0) {
            return Result.success();
        } else {
            return Result.build(ResultCode.OP_ERROR);
        }
    }

    @PostMapping("/delete")
    public Result delete(@Validated(Task.Update.class) @RequestBody Task task) {
        if (taskService.deleteTask(task.getTaskId()) > 0) {
            return Result.success();
        } else {
            return Result.build(ResultCode.OP_ERROR);
        }
    }

    @PostMapping("/saveUser")
    public Result saveUser(@RequestParam Integer userId, String roleId) {
        if (taskService.saveUser(userId, roleId) > 0) {
            return Result.success();
        } else {
            return Result.build(ResultCode.OP_ERROR);
        }
    }

}
