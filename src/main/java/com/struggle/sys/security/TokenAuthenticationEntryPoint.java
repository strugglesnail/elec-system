package com.struggle.sys.security;

import com.struggle.sys.common.TokenResponse;
import com.struggle.sys.util.JwtUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;

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
public class TokenAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException exception) {
        ServerHttpResponse response = exchange.getResponse();
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
            error = TokenResponse.createByErrorMessage(exception.getMessage());
        }
        return JwtUtils.writerToJson(response, error);
    }

}
