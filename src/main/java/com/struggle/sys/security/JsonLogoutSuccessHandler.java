package com.struggle.sys.security;

import com.struggle.sys.common.Constants;
import com.struggle.sys.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @Author WTF
 * @Date 2020/2/18 10:48
 * @Description 登出成功回调
 */
@Component
public class JsonLogoutSuccessHandler implements ServerLogoutSuccessHandler {

    @Autowired
    private RedisService redisService;

    // 登出成功操作
    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        // 清除token缓存
        redisService.delete(Constants.JWT_ACCESS_TOKEN_KEY);
        redisService.delete(Constants.JWT_REFRESH_TOKEN_KEY);
        ServerWebExchange exchange1 = exchange.getExchange();
//        exchange1.
        return new DefaultServerRedirectStrategy().sendRedirect(exchange1, URI.create("/logout"));
    }
}
