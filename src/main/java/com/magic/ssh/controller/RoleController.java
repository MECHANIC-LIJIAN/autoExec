package com.magic.ssh.controller;

import com.magic.ssh.entity.security.SysRole;
import com.magic.ssh.service.RoleService;
import com.magic.ssh.util.Result;
import com.magic.ssh.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/queryAll")
    public Result queryAll(){
        List<SysRole> list=roleService.getAll();
        if (list.size()<=0) {
            return Result.build(ResultCode.OP_ERROR);
        } else {
            return Result.success(list);
        }
    }


    @GetMapping("/queryById")
    public Result queryById(@RequestParam Integer roleId){
        SysRole sysRole=roleService.getRoleById(roleId);
        if (null == sysRole) {
            return Result.build(ResultCode.OP_ERROR);
        } else {
            return Result.success(sysRole);
        }
    }

    @PostMapping("/add")
    public Result add(@RequestBody SysRole pSysRole){
        SysRole sysRole=roleService.insertRole(pSysRole);
        if (null == sysRole) {
            return Result.build(ResultCode.OP_ERROR);
        } else {
            return Result.success(sysRole);
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody SysRole pSysRole){
        SysRole sysRole=roleService.updateRole(pSysRole);
        if (null == sysRole) {
            return Result.build(ResultCode.OP_ERROR);
        } else {
            return Result.success(sysRole);
        }
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer roleId){
        if (roleService.deleteRole(roleId)) {
            return Result.build(ResultCode.OP_ERROR);
        } else {
            return Result.success();
        }
    }
}
