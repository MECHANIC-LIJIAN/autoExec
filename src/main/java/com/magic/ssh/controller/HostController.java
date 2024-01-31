package com.magic.ssh.controller;

import com.magic.ssh.entity.Host;
import com.magic.ssh.service.HostService;
import com.magic.ssh.util.Result;
import com.magic.ssh.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/api/host")
public class HostController {

    @Autowired
    private HostService hostService;

    @GetMapping("/query")
    public Result guery(@RequestParam String hostId) {
        Host host = hostService.getHostInfoById(hostId);
        if (Objects.isNull(host)) {
            return Result.build(ResultCode.OP_ERROR);
        } else {
            return Result.success(host);
        }
    }

    @GetMapping("/queryByUser")
    public Result gueryRoles(@RequestParam Integer userId) {

        List<Host> list = hostService.getHostByUser(userId);
        if (list.isEmpty()) {
            return Result.build(ResultCode.OP_ERROR);
        } else {
            return Result.success(list);
        }
    }

    @PostMapping("/add")
    public Result add(@Validated(Host.Create.class) @RequestBody Host host) {
        try {
            if (hostService.insertHost(host)>0) {
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
        if (hostService.updateHost(host)>0) {
            return Result.success();
        } else {
            return Result.build(ResultCode.OP_ERROR);
        }
    }

    @PostMapping("/delete")
    public Result delete(@Validated(Host.Update.class) @RequestBody Host host) {
        if (hostService.deleteHost(host.getHostId())>0) {
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
