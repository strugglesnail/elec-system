package com.struggle.sys.security;

import com.struggle.sys.common.ServerResponse;
import com.struggle.sys.common.TokenResponse;
import com.struggle.sys.util.JwtUtils;
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
public class TokenAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        TokenResponse<Object> error;
        if (exception instanceof InsufficientAuthenticationException) {
            error = TokenResponse.createByErrorMessage(exception.getMessage());
        } else if (exception instanceof CredentialsExpiredException) {
            error = TokenResponse.createByLoginExpired();
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            error = TokenResponse.createByUnauthorized();
        } else if (exception instanceof AuthenticationServiceException) {
            error = TokenResponse.createByNoLogin();
        } else {
            error = TokenResponse.createByErrorMessage("访问失败!");
        }
        JwtUtils.writerToJson(response, error);
    }
}
