package com.td.server.controller;

import com.td.server.dto.base.ResponseResult;
import com.td.server.dto.LoginParam;
import com.td.server.service.SysLoginService;
import com.td.server.service.SysUserService;
import com.td.server.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Td
 * @ClassName SysLoginController
 * @date 2023-12-04
 */
@RestController
public class SysLoginController {

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private SysUserService userService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody LoginParam loginParam) {
        return ResponseResult.success("登录成功", loginService.login(loginParam));
    }


    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
//    @GetMapping("getInfo")
//    public ResponseResult getInfo() {
//        SysUser user = SecurityUtils.getLoginUser().getUser();
//        // 角色集合
//        Set<String> roles = permissionService.getRolePermission(user);
//        // 权限集合
//        Set<String> permissions = permissionService.getMenuPermission(user);
//        ResponseResult ajax = ResponseResult.success();
//        ajax.put("user", user);
//        ajax.put("roles", roles);
//        ajax.put("permissions", permissions);
//        return ajax;
//    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("/getRouters")
    public ResponseResult getRouters() {
        return ResponseResult.success(userService.getUserRouterMenus(SecurityUtils.getUserId()));
    }
}
