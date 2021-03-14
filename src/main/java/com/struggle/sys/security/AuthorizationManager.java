package com.struggle.sys.security;

import com.alibaba.fastjson.JSONObject;
import com.struggle.sys.common.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException;
import java.util.Collection;

/**
 * @author strugglesnail
 * @date 2021/3/10
 * @desc  授权管理器：给请求授权
 */
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationManager.class);

    // 请求路径匹配器
    private AntPathMatcher antPathMatcher = new AntPathMatcher();


    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> mono, AuthorizationContext authorizationContext) {
        ServerWebExchange exchange = authorizationContext.getExchange();
        String path = exchange.getRequest().getPath().value();
        return mono.map((authentication -> {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                ResourcesGrantedAuthority resourcesAuthority = (ResourcesGrantedAuthority) authority;
                boolean isMatch = resourcesAuthority.getUrls().stream().anyMatch(url -> antPathMatcher.match(url, path));
                if (isMatch) {
                    logger.info(String.format("用户请求API校验通过，Path:{%s}   GrantedAuthority:{%s}", path, resourcesAuthority.getUrls()));
                    return new AuthorizationDecision(true);
                }
            }
            return new AuthorizationDecision(false);
        })).defaultIfEmpty(new AuthorizationDecision(false));
    }

    @Override
    public Mono<Void> verify(Mono<Authentication> authentication, AuthorizationContext object) {
        return check(authentication, object)
                .filter(a -> a.isGranted())
                .switchIfEmpty(Mono.defer(() -> {
                    ServerResponse<Object> response = ServerResponse.createByErrorMessage("当前用户没有访问权限！");
                    return Mono.error(new AccessDeniedException(JSONObject.toJSONString(response)));
                })).flatMap(a -> Mono.empty());
    }
}
