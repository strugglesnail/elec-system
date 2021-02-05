package com.struggle.sys.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.struggle.sys.common.ServerResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author WTF
 * @Date 2020/2/18 10:42
 * @Description 登录失败回调
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println("username：" + username);
        System.out.println("password：" + password);
        ServerResponse error = ServerResponse.createByErrorMessage("登录失败！");
        if (exception instanceof LockedException) {
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
