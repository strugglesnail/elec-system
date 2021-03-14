package com.struggle.sys.filter;

import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.web.server.authorization.AuthorizationWebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author strugglesnail
 * @date 2021/3/10
 * @desc
 */
public class JwtAuthorizationFilter extends AuthorizationWebFilter {

    public JwtAuthorizationFilter(ReactiveAuthorizationManager<? super ServerWebExchange> accessDecisionManager) {
        super(accessDecisionManager);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange);
    }
}
