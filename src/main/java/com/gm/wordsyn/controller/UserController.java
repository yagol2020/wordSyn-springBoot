package com.gm.wordsyn.controller;

import com.gm.wordsyn.entity.User;
import com.gm.wordsyn.result.Result;
import com.gm.wordsyn.result.ResultFactory;
import com.gm.wordsyn.service.AdminUserRoleService;
import com.gm.wordsyn.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AdminUserRoleService adminUserRoleService;

    @GetMapping("/api/admin/user")
    public Result listUsers() {
        return ResultFactory.buildSuccessResult(userService.list());
    }

    @GetMapping("/api/user/get")
    public Result getUserByUserName(@RequestParam("username") String username){
        return ResultFactory.buildSuccessResult(userService.findByUsername(username));
    }

    @PutMapping("/api/admin/user/status")
    public Result updateUserStatus(@RequestBody User requestUser) {
        if (userService.updateUserStatus(requestUser)) {
            return ResultFactory.buildSuccessResult("用户状态更新成功");
        } else {
            return ResultFactory.buildFailResult("参数错误，更新失败");
        }
    }

    @PutMapping("/api/admin/user/password")
    public Result resetPassword(@RequestBody User requestUser) {
        if (userService.resetPassword(requestUser)) {
            return ResultFactory.buildSuccessResult("重置密码成功");
        } else {
            return ResultFactory.buildFailResult("参数错误，重置失败");
        }
    }

    @PutMapping("/api/admin/user")
    public Result editUser(@RequestBody User requestUser) {
        if (userService.editUser(requestUser)) {
            return ResultFactory.buildSuccessResult("修改用户信息成功");
        } else {
            return ResultFactory.buildFailResult("参数错误，修改失败");
        }
    }

    @PutMapping("/api/user/edit")
    public Result editUser(@RequestParam("username") String username,@RequestParam("phone") String phone,
                           @RequestParam("email") String email) {
        if(userService.editUser(username,phone,email)){
            return ResultFactory.buildSuccessResult("edit OK");
        }
        else{
            return ResultFactory.buildFailResult("edit error");
        }
    }


    @PostMapping("/api/admin/deleteUser")
    public Result deleteUser(@RequestParam(value = "uid") int uid){
        adminUserRoleService.deleteDataByUserId(uid);
        userService.deleteDataByUserId(uid);
        return ResultFactory.buildSuccessResult("OK");
    }
}
