package com.magic.ssh.controller;

import com.magic.ssh.entity.Action;
import com.magic.ssh.entity.Host;
import com.magic.ssh.service.ActionService;
import com.magic.ssh.service.HostService;
import com.magic.ssh.util.Result;
import com.magic.ssh.util.ResultCode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Null;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/host")
public class HostController {

    private final HostService hostService;

    private final ActionService actionService;

    public HostController(HostService hostService, ActionService actionService) {
        this.hostService = hostService;
        this.actionService = actionService;
    }

    @GetMapping("/query")
    public Result guery(@RequestParam String hostId) {
        Host host = hostService.getHostInfoById(hostId);
        if (Objects.isNull(host)) {
            return Result.build(ResultCode.RESOURCE_NOT_EXIST);
        } else {
            host.setPassword(null);
            return Result.success(host);
        }
    }

    @GetMapping("/queryByUser")
    public Result gueryRoles(@RequestParam Integer userId) {
        return  Result.success(hostService.getHostByUser(userId));
    }

    @PostMapping("/add")
    public Result add(@Validated(Host.Create.class) @RequestBody Host host) {
        try {
            if (hostService.insertHost(host) > 0) {
                return Result.success();
            } else {
                return Result.build(ResultCode.OP_ERROR);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/update")
    public Result update(@Validated(Host.Update.class) @RequestBody Host host) {
        if (hostService.updateHost(host) > 0) {
            return Result.success();
        } else {
            return Result.build(ResultCode.OP_ERROR);
        }
    }

    @PostMapping("/delete")
    public Result delete(@Validated(Host.Update.class) @RequestBody Host host) {

        Action action = actionService.getActionByHost(host.getHostId());
        if (Objects.nonNull(action)) {
            return Result.build(ResultCode.OP_ERROR, "存在关联的动作 " + action.getActionName() + ",无法删除该主机",
                    "");
        }

        if (hostService.deleteHost(host.getHostId()) > 0) {
            return Result.success();
        } else {
            return Result.build(ResultCode.OP_ERROR);
        }
    }

    @PostMapping("/saveUser")
    public Result saveUser(@RequestParam Integer userId, String roleId) {
        if (hostService.saveUser(userId, roleId) > 0) {
            return Result.success();
        } else {
            return Result.build(ResultCode.OP_ERROR);
        }
    }

}
