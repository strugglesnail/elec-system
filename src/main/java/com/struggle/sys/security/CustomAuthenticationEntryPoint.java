package com.struggle.sys.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.struggle.sys.common.ServerResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author WTF
 * @Date 2020/2/18 10:50
 * @Description 用户没有认证时统一认证
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        ServerResponse<Object> error = ServerResponse.createByErrorMessage("访问失败!");
        if (exception instanceof InsufficientAuthenticationException) {
            error = ServerResponse.createByErrorMessage(exception.getMessage());
        } else if (exception instanceof UsernameNotFoundException) {
            error = ServerResponse.createByErrorMessage(exception.getMessage());
        } else if (exception instanceof CredentialsExpiredException) {
            error = ServerResponse.createByErrorMessage(exception.getMessage());
        } else if (exception instanceof LockedException) {
            error = ServerResponse.createByErrorMessage("账户被锁定，请联系管理员!");
        } else if (exception instanceof CredentialsExpiredException) {
            error = ServerResponse.createByErrorMessage("密码过期，请联系管理员!");
        } else if (exception instanceof AccountExpiredException) {
            error = ServerResponse.createByErrorMessage("账户过期，请联系管理员!");
        } else if (exception instanceof DisabledException) {
            error = ServerResponse.createByErrorMessage("账户被禁用，请联系管理员!");
        } else if (exception instanceof BadCredentialsException) {
            error = ServerResponse.createByErrorMessage("用户名或者密码输入错误，请重新输入!");
        }
        out.write(new ObjectMapper().writeValueAsString(error));
        out.flush();
        out.close();
    }
}
