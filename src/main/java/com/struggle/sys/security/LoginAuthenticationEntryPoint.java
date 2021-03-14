package com.struggle.sys.security;

import com.struggle.sys.common.ServerResponse;
import com.struggle.sys.util.JwtUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author WTF
 * @Date 2020/2/18 10:50
 * @Description 用户没有认证时统一认证
 */
@Component
public class LoginAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {


    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException exception) {
        ServerHttpResponse response = exchange.getResponse();

        ServerResponse<Object> error = ServerResponse.createByErrorMessage(exception.getMessage());
        if (exception instanceof AuthenticationCredentialsNotFoundException) {
            error = ServerResponse.createByErrorMessage(exception.getMessage());
        } else if (exception instanceof LockedException) {
            error = ServerResponse.createByErrorMessage("账户被锁定，请联系管理员!");
        } else if (exception instanceof CredentialsExpiredException) {
            error = ServerResponse.createByErrorMessage("密码过期，请联系管理员!");
        } else if (exception instanceof AccountExpiredException) {
            error = ServerResponse.createByErrorMessage("账户过期，请联系管理员!");
        } else if (exception instanceof DisabledException) {
            error = ServerResponse.createByErrorMessage("账户被禁用，请联系管理员!");
        } else if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            error = ServerResponse.createByErrorMessage("用户名或者密码输入错误，请重新输入!");
        }
        return JwtUtils.writerToJson(response, error);
    }

}
