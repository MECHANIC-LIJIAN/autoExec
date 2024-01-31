package com.magic.ssh.controller;

import com.magic.ssh.entity.security.SysRole;
import com.magic.ssh.entity.security.SysUser;
import com.magic.ssh.service.UserService;
import com.magic.ssh.util.Result;
import com.magic.ssh.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/query")
    public Result guery(@RequestParam String username) {
        SysUser sysUser=userService.getUserInfoByUserName(username);
        if (null == sysUser) {
            return Result.build(ResultCode.OP_ERROR);
        } else {
            return Result.success(sysUser);
        }
    }

    @GetMapping("/queryRole")
    public Result gueryRoles(@RequestParam String username) {

        List<SysRole> list=userService.getUserRoleByUserName(username);
        if (list.size()<=0) {
            return Result.build(ResultCode.OP_ERROR);
        } else {
            return Result.success(list);
        }
    }


    @PostMapping("/add")
    public Result add(@RequestBody SysUser pSysUser) {
        if (userService.insertUser(pSysUser)>0) {
            return Result.success();
        } else {
            return Result.build(ResultCode.OP_ERROR);
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody SysUser pSysUser){
        if (userService.updateUser(pSysUser)>0) {
            return Result.success();
        } else {
            return Result.build(ResultCode.OP_ERROR);
        }
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam String username){
        if (userService.deleteUser(username)>0) {
            return Result.success();
        } else {
            return Result.build(ResultCode.OP_ERROR);
        }
    }

    @PostMapping("/saveRole")
    public Result saveRole(@RequestParam Integer userId,String roleId){

        if (userService.saveRole(userId,roleId)>0) {
            return Result.build(ResultCode.OP_ERROR);
        } else {
            return Result.success();
        }
    }

}
