package com.td.server.handler.security;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.td.server.dto.base.LoginUser;
import com.td.server.exception.ServiceException;
import com.td.server.pojo.SysRole;
import com.td.server.pojo.SysUser;
import com.td.server.service.SysUserService;
import com.td.server.utils.AuthenticationContextHolder;
import com.td.server.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Td
 * @ClassName AccessUserDetailsServiceImpl
 * @description: TODO
 * @date 2023-12-04
 * @version: 1.0
 */
@Slf4j
@Primary
@Service("AccessUserDetailsService")
public class AccessUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        SysUser sysUser = userService.getOne(
                Wrappers.lambdaQuery(SysUser.class)
                        .eq(SysUser::getUsername, username));

        if (sysUser == null) {
            throw new ServiceException("用户名不存在！");
        }

        Authentication usernamePasswordAuthenticationToken = AuthenticationContextHolder.getContext();
        String password = usernamePasswordAuthenticationToken.getCredentials().toString();

        if (!SecurityUtils.matchesPassword(password, sysUser.getPassword())) {
            throw new ServiceException("密码错误！");
        }

        //查询用户的权限列表
        Set<String> permission = userService.getUserPermission(sysUser.getId());

        //查询角色信息
        Set<SysRole> roles = new HashSet<>();
        return new LoginUser(sysUser.getId(), sysUser, permission, roles);
    }
}
