package com.struggle.sys.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author WTF
 * @Date 2020/2/18 10:42
 * @Description 登录失败回调
 */
@Component
public class JsonAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {

    @Autowired
    private LoginAuthenticationEntryPoint authenticationEntryPoint;


    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        return authenticationEntryPoint.commence(exchange, exception);
    }
}
