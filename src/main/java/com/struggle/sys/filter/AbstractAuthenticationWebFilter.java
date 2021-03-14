package com.struggle.sys.filter;


import com.struggle.sys.common.ServerResponse;
import com.struggle.sys.security.AuthenticationConverter;
import com.struggle.sys.util.JwtUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.*;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


public abstract class AbstractAuthenticationWebFilter implements WebFilter {

    // 认证管理器
    private ReactiveAuthenticationManager authenticationManager;

    // 登录器
    private ServerFormLoginAuthenticationConverter authenticationConverter = new AuthenticationConverter();

    // 成功处理器
    private ServerAuthenticationSuccessHandler authenticationSuccessHandler = new WebFilterChainServerAuthenticationSuccessHandler();

    // 失败处理器
    private ServerAuthenticationFailureHandler authenticationFailureHandler = new ServerAuthenticationEntryPointFailureHandler(new HttpBasicServerAuthenticationEntryPoint());

    // 设置上下文
    private ServerSecurityContextRepository securityContextRepository = NoOpServerSecurityContextRepository.getInstance();

    // 认证匹配器
    private ServerWebExchangeMatcher requiresAuthenticationMatcher;



    // 初始化过滤器
    public AbstractAuthenticationWebFilter(String defaultFilterProcessesUrl, ReactiveAuthenticationManager authenticationManager) {
        Assert.notNull(authenticationManager, "authenticationManager cannot be null");
        setRequiresAuthenticationMatcher(new PathPatternParserServerWebExchangeMatcher(defaultFilterProcessesUrl));
        this.authenticationManager = authenticationManager;
    }

    // 过滤信息
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        WebFilterExchange webFilterExchange = new WebFilterExchange(exchange, chain);

        Authentication authResult;
        if (!requiresAuthenticationMatcher.matches(exchange).block().isMatch()) {
            return chain.filter(exchange);
        } else {
            try {
                authResult = this.attemptAuthentication(exchange);
                if (authResult == null) {
                    return Mono.empty();
                }
            } catch (InternalAuthenticationServiceException exception) {
                return this.onAuthenticationFail(webFilterExchange, exception.getMessage());
            } catch (AuthenticationException exception) {
                return this.onAuthenticationFail(webFilterExchange, exception.getMessage());
            }
        }
        return onAuthenticationSuccess(authResult, webFilterExchange);
//        return this.requiresAuthenticationMatcher.matches(exchange)
//                .filter((matchResult) -> matchResult.isMatch())
//                .flatMap((matchResult) -> this.authenticationConverter.convert(exchange))
//                .switchIfEmpty(chain.filter(exchange).then(Mono.empty()))
//                .flatMap((token) -> this.authenticate(exchange, chain, token));
    }

    private Mono<Void> authenticate(ServerWebExchange exchange, WebFilterChain chain, Authentication token) {
        WebFilterExchange webFilterExchange = new WebFilterExchange(exchange, chain);
        return ((ReactiveAuthenticationManagerResolver<ServerHttpRequest>) request -> Mono.just(authenticationManager))
                .resolve(exchange.getRequest()).flatMap((authenticationManager) -> authenticationManager.authenticate(token)).switchIfEmpty(Mono.defer(() -> {
            return Mono.error(new IllegalStateException("No provider found for " + token.getClass()));
        })).flatMap((authentication) -> {
            return this.onAuthenticationSuccess(authentication, webFilterExchange);
        }).onErrorResume(AuthenticationException.class, (e) -> {
            return this.authenticationFailureHandler.onAuthenticationFailure(webFilterExchange, e);
        });
    }


    // 认证成功
    protected Mono<Void> onAuthenticationSuccess(Authentication authentication, WebFilterExchange webFilterExchange) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        return this.securityContextRepository.save(exchange, securityContext).then(this.authenticationSuccessHandler.onAuthenticationSuccess(webFilterExchange, authentication)).subscriberContext(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
    }

    // 认证失败
    protected Mono<Void> onAuthenticationFail(WebFilterExchange exchange, String errorMsg) {
        return JwtUtils.writerToJson(exchange.getExchange().getResponse(), ServerResponse.createByErrorMessage(errorMsg));
    }

    // 自定义上下文库
    public void setSecurityContextRepository(ServerSecurityContextRepository securityContextRepository) {
        Assert.notNull(securityContextRepository, "securityContextRepository cannot be null");
        this.securityContextRepository = securityContextRepository;
    }

    // 自定义认证成功处理器
    public void setAuthenticationSuccessHandler(ServerAuthenticationSuccessHandler authenticationSuccessHandler) {
        Assert.notNull(authenticationSuccessHandler, "authenticationSuccessHandler cannot be null");
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    // 自定义认证失败处理器
    public void setAuthenticationFailureHandler(ServerAuthenticationFailureHandler authenticationFailureHandler) {
        Assert.notNull(authenticationFailureHandler, "authenticationFailureHandler cannot be null");
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    // 请求路径匹配
    public void setRequiresAuthenticationMatcher(ServerWebExchangeMatcher requiresAuthenticationMatcher) {
        Assert.notNull(requiresAuthenticationMatcher, "requiresAuthenticationMatcher cannot be null");
        this.requiresAuthenticationMatcher = requiresAuthenticationMatcher;
    }

    public ReactiveAuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public abstract Authentication attemptAuthentication(ServerWebExchange exchange);

}

