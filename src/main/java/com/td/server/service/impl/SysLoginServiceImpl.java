package com.td.server.service.impl;

import com.td.server.dto.LoginParam;
import com.td.server.dto.base.LoginUser;
import com.td.server.dto.constant.Constants;
import com.td.server.exception.ServiceException;
import com.td.server.service.SysLoginService;
import com.td.server.utils.AuthenticationContextHolder;
import com.td.server.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Td
 * @ClassName SysLoginServiceImpl
 * @description: TODO
 * @date 2023-12-04
 * @version: 1.0
 */
@Service
@Slf4j
public class SysLoginServiceImpl implements SysLoginService {

    @Autowired
    private TokenUtils tokenUtils;

    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public String login(LoginParam loginParam) {
        // 用户验证
        Authentication authentication;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginParam.getUsername(), loginParam.getPassword());
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);

        } catch (Exception e) {
            log.info("登录异常信息: ", e);
            throw new ServiceException(e.getMessage());
        }
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //生成token
        return tokenUtils.createToken(loginUser);
    }
}
