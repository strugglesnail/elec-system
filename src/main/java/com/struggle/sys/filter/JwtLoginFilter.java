package com.struggle.sys.filter;

import com.struggle.sys.security.AuthenticationConverter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerFormLoginAuthenticationConverter;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author strugglesnail
 * @date 2021/3/2
 * @desc
 */
public class JwtLoginFilter extends AuthenticationWebFilter {
    private String usernameParameter = "username";
    private String passwordParameter = "password";
    private String defaultLoginUrl = "/login";

    private ServerFormLoginAuthenticationConverter authenticationConverter = new AuthenticationConverter();


    public JwtLoginFilter(String defaultFilterProcessesUrl, ReactiveAuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.defaultLoginUrl = defaultFilterProcessesUrl;
//        setRequiresAuthenticationMatcher(new PathPatternParserServerWebExchangeMatcher(defaultFilterProcessesUrl));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String requestPath = exchange.getRequest().getPath().value();
        if (defaultLoginUrl.equals(requestPath)) {
            return super.filter(exchange, chain);
        }
        return chain.filter(exchange);

    }

    //    @Override
//    protected Mono<Void> onAuthenticationSuccess(Authentication authentication, WebFilterExchange webFilterExchange) {
//
////        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
////        String name = authentication.getName();
//////        String[] split = name.split(",");
////        // 用户Id
////        Long userId = 1L;// Long.valueOf(split[0]);
////        // 用户账号
////        String account = name;
////
////        UserDetails userDetails = User.builder().username(name).password("").authorities(authorities).build();
////
////        // 创建accessToken
////        String accessToken = JwtUtils.createAccessToken(userDetails);
////
////        // 创建refreshToken
////        String refreshToken = JwtUtils.createRefreshToken(account);
////
////        return JwtUtils.writerToJson(webFilterExchange.getExchange().getResponse(), TokenResponse.createBySuccessMessage(accessToken, refreshToken, userId));
//        return Mono.empty();
//    }
//
//    @Override
//    public Authentication attemptAuthentication(ServerWebExchange exchange) throws AuthenticationException {
//
//        ServerHttpRequest request = exchange.getRequest();
//
//        exchange.getFormData().map((data) -> {
//            String username = data.getFirst(this.usernameParameter);
//            String password = data.getFirst(this.passwordParameter);
//            return new UsernamePasswordAuthenticationToken(username, password);
//        });
//
//        Mono<Authentication> mono = authenticationConverter.convert(exchange);
//        Authentication authentication = mono.block();
//
//        String username = authentication.getName();
//        String password = (String) authentication.getCredentials();
//        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
//            throw new AuthenticationCredentialsNotFoundException("账号密码不为空!");
//        }
//        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(username, password)).block();
//    }
//
//    public static void main(String[] args) {
//        System.out.println();
//    }
}
