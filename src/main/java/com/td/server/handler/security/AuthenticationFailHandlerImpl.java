package com.td.server.handler.security;

import com.alibaba.fastjson.JSON;
import com.td.server.dto.base.HttpStatus;
import com.td.server.dto.base.ResponseResult;
import com.td.server.utils.ServletUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

@Component
public class AuthenticationFailHandlerImpl implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        int code = HttpStatus.UNAUTHORIZED;
        String msg = "请求访问：" + request.getRequestURI() + "，认证失败，无法访问系统资源";
        ServletUtils.renderString(response, JSON.toJSONString(ResponseResult.error(code, msg)));
    }
}
