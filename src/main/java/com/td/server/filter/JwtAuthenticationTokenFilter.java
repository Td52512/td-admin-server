package com.td.server.filter;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.td.server.dto.base.LoginUser;
import com.td.server.utils.SecurityUtils;
import com.td.server.utils.TokenUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Td
 * @ClassName AuthenticationTokenFilter
 * @description: token过滤器 验证token有效性
 * @date 2023-12-04
 */
@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private TokenUtils tokenUtils;
    ;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {

//        log.info("进入了AuthenticationTokenFilter");
//        Authentication authentication = SecurityUtils.getAuthentication();
        LoginUser loginUser = tokenUtils.getLoginUser(request);
        if (ObjectUtils.isNotNull(loginUser) && ObjectUtils.isNull(SecurityUtils.getAuthentication())) {
            tokenUtils.verifyToken(loginUser);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        chain.doFilter(request, response);
    }
}
