package com.magic.ssh.controller;

import com.alibaba.excel.EasyExcel;
import com.magic.ssh.SysConstants;
import com.magic.ssh.entity.Action;
import com.magic.ssh.entity.Host;
import com.magic.ssh.service.ActionService;
import com.magic.ssh.service.HostService;
import com.magic.ssh.util.Result;
import com.magic.ssh.util.ResultCode;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

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
        Host host = hostService.getHostById(hostId);
        if (Objects.isNull(host)) {
            return Result.build(ResultCode.RESOURCE_NOT_EXIST);
        } else {
            host.setPassword(null);
            return Result.success(host);
        }
    }

    @GetMapping("/queryByUser")
    public Result gueryRoles(@RequestParam Integer userId) {
        return Result.success(hostService.getHostByUser(userId));
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



    @PostMapping("/import")
    public Result importExcel(@RequestParam("file") MultipartFile excel) {
        try {
            List<Host> hostList = EasyExcel.read(excel.getInputStream())
                    .head(Host.class)
                    .sheet()
                    .doReadSync();
            for (Host host : hostList) {
                if (!host.importChk()){
                    return Result.build(ResultCode.OP_ERROR,"存在空缺数据行",host);
                }
            }

            for (Host host : hostList) {
                int ret = hostService.insertHost(host);
                if (ret== SysConstants.RESOURCE_DP) {
                    String errMsg = String.format( "主机名【%s】已存在 或者 主机【%s/%s】已存在",host.getHostname(),host.getIpAddress(),
                            host.getUsername() );
                    return Result.build(ResultCode.OP_ERROR,errMsg, "");
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success("上传成功");
    }

}
