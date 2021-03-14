package com.struggle.sys.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerFormLoginAuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author strugglesnail
 * @date 2021/3/5
 * @desc
 */
public class AuthenticationConverter extends ServerFormLoginAuthenticationConverter {

    private String usernameParameter = "username";

    private String passwordParameter = "password";

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        MultiValueMap<String, String> queryParams = exchange.getRequest().getQueryParams();
        return Mono.just(new UsernamePasswordAuthenticationToken(queryParams.getFirst(usernameParameter), queryParams.getFirst(passwordParameter)));
    }
}
