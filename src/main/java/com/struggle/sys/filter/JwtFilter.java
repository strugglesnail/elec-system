package com.struggle.sys.filter;

import com.struggle.sys.common.Constants;
import com.struggle.sys.security.LoginAuthenticationEntryPoint;
import com.struggle.sys.security.TokenAuthenticationEntryPoint;
import com.struggle.sys.service.RedisService;
import com.struggle.sys.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @auther strugglesnail
 * @date 2021/1/25 23:21
 * @desc
 */
public class JwtFilter extends GenericFilterBean {

    @Autowired
    private TokenAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private RedisService redisService;

    // 用户信息


    // token过滤
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 从头信息获取refreshToken
        String refreshToken = request.getHeader("RefreshToken");

        // 从头信息获取accessToken
        String accessToken = request.getHeader("Authorization");

        // 从redis获取refreshToken(如果过期，则获取的Token为空)
        String cacheRefreshToken = redisService.get(Constants.JWT_REFRESH_TOKEN_REDIS_KEY, String.class);

        // 判断refreshToken是否存在：如果存在，则继续执行，否则该请求没有授权
        if (StringUtils.isEmpty(refreshToken) || StringUtils.isEmpty(accessToken)) {
            authenticationEntryPoint.commence(request, response, new AuthenticationCredentialsNotFoundException(null));
            return;
        }

        // 判断cacheRefreshToken是否存在：如果不存在，则说明过期需要重新登陆
        if (StringUtils.isEmpty(cacheRefreshToken)) {
            authenticationEntryPoint.commence(request, response, new CredentialsExpiredException(null));
            return;
        }

        // 客户端refreshToken与服务端refreshToken不一致
        if (!cacheRefreshToken.equals(refreshToken)) {
            authenticationEntryPoint.commence(request, response, new InsufficientAuthenticationException("身份验证信息不足"));
            return;
        }


        // 从redis获取accessToken
        String cacheAccessToken = redisService.get(Constants.JWT_ACCESS_TOKEN_REDIS_KEY, String.class);


        // 如果accessToken不存在，则已经过期，需要重新创建accessToken
        if (StringUtils.isEmpty(cacheAccessToken)) {
            UserDetails userDetails = JwtUtils.getUserDetails();
            accessToken = JwtUtils.createAccessToken(userDetails);
        }
        // 1、如果已经过期，则重新创建accessToken
        // 2、如果未过期，则放开请求API




        if (accessToken.startsWith("Bearer")) {
            // 获取当前用户信息
            Claims claims = getClaims(accessToken);

            // 获取当前登录的用户
            String username = claims.getSubject();

            // 获取当前用户权限
            List<GrantedAuthority> authorities =
                    AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));

            // 保存到上下文
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, authorities));
        }

        filterChain.doFilter(request, servletResponse);
    }

    // 解析Token获取用户信息
    private Claims getClaims(String token) throws CredentialsExpiredException {
        Claims claims = null;
        token = token.replace("Bearer", "");
        try {
            claims = JwtUtils.parseToken(token);
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("Token Time Out!");
        } catch (MalformedJwtException e) {
            throw new InsufficientAuthenticationException("Token Format Error!");
        }
        return claims;
    }
}
