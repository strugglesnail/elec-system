package com.struggle.sys.filter;

import com.struggle.sys.common.Constants;
import com.struggle.sys.security.TokenAuthenticationEntryPoint;
import com.struggle.sys.service.RedisService;
import com.struggle.sys.service.UserService;
import com.struggle.sys.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @auther strugglesnail
 * @date 2021/1/25 23:21
 * @desc
 */
public class JwtFilter implements WebFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    public static int c = 0;

    @Autowired
    private TokenAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;



    // token过滤
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 从头信息获取refreshToken
        String refreshToken = request.getHeaders().getFirst(Constants.JWT_HEAD_REFRESH_TOKEN);

        // 从头信息获取accessToken
        String accessToken = request.getHeaders().getFirst(Constants.JWT_HEAD_ACCESS_TOKEN);


        // 1、判断refreshToken、accessToken是否为空
        // 判断refreshToken是否存在：如果存在，则继续执行，否则该请求没有授权
        if (StringUtils.isEmpty(refreshToken) || StringUtils.isEmpty(accessToken)) {
            logger.info("header请求头获取不到refreshToken或accessToken");
            return authenticationEntryPoint.commence(exchange, new AuthenticationCredentialsNotFoundException(null));

        }

        // 根据token获取用户Id
        Claims claims = getClaims(exchange, accessToken);
        Long userId = claims.get("userId", Long.class);

        // 从redis获取accessToken
        String cacheAccessToken = redisService.get(JwtUtils.getAccessTokenKey(userId));


        // 从redis获取refreshToken(如果过期，则获取的Token为空)
        String cacheRefreshToken = redisService.get(JwtUtils.getRefreshTokenKey(userId));



        // 2、判断cacheAccessToken、cacheRefreshToken是否为空
        // 为空的话说明没有登录
        if (StringUtils.isEmpty(cacheAccessToken) || StringUtils.isEmpty(cacheRefreshToken)) {
            logger.info("没有登录");
            return authenticationEntryPoint.commence(exchange, new AuthenticationServiceException(null));
        }

        String path = request.getPath().value();

        // 2、验证客户段的token是否与服务器端的一致
        // 【客户端refreshToken与服务端refreshToken不一致】 或者 【客户端accessToken与服务端accessToken不一致】
        if (!cacheRefreshToken.equals(refreshToken) || !cacheAccessToken.equals(accessToken)) {
            logger.info("验证信息有误");
            return authenticationEntryPoint.commence(exchange, new InsufficientAuthenticationException("身份验证信息不足"));
        }

        logger.info( + (++c) + " : accessToken=" + accessToken.split("[.]")[2].substring(0, 4) + " cacheAccessToken=" + cacheAccessToken.split("[.]")[2].substring(0, 4));


        // 3、验证refreshToken是否过期
        // refreshToken过期，则需要重新登陆
        if (JwtUtils.isTokenExpired(refreshToken)) {
            logger.info("refreshToken过期，则需要重新登陆");
            return authenticationEntryPoint.commence(exchange, new CredentialsExpiredException(null));
        }



        // 根据token获取用户信息
        // accessToken过期，需要重新创建accessToken
        if (JwtUtils.isTokenExpired(JwtUtils.removeBearer(cacheAccessToken))) {
            logger.info("expire"+path + (c) + " : accessToken=" + accessToken.split("[.]")[2].substring(0, 4) + " cacheAccessToken=" + cacheAccessToken.split("[.]")[2].substring(0, 4));

            // 获取数据库用户信息
            UserDetails userDetails = userService.loadUserByUserId(userId);
            // 创建新的token
            accessToken = JwtUtils.createAccessToken(userDetails);
            redisService.set(JwtUtils.getAccessTokenKey(userId), accessToken);
            response.getHeaders().set(Constants.JWT_HEAD_ACCESS_TOKEN, accessToken);
        }
        return chain.filter(exchange);
    }




    // 解析Token获取用户信息
    private Claims getClaims(ServerWebExchange exchange, String token) throws CredentialsExpiredException {
        Claims claims = null;
        token = token.replace(Constants.JWT_BEARER, "");
        try {
            claims = JwtUtils.parseToken(token);
        } catch (ExpiredJwtException e) {
//            authenticationEntryPoint.commence(request, response,new CredentialsExpiredException("Token Time Out!"));
            return e.getClaims();
        } catch (MalformedJwtException e) {
            authenticationEntryPoint.commence(exchange,new InsufficientAuthenticationException("Token Format Error!"));
        }
        return claims;
    }

}
