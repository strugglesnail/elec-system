package com.struggle.sys.filter;

import com.struggle.sys.common.Constants;
import com.struggle.sys.pojo.SysUser;
import com.struggle.sys.security.LoginAuthenticationEntryPoint;
import com.struggle.sys.security.TokenAuthenticationEntryPoint;
import com.struggle.sys.service.RedisService;
import com.struggle.sys.service.SysUserService;
import com.struggle.sys.service.UserService;
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

    @Autowired
    private UserService userService;

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

        // 从redis获取accessToken
        String cacheAccessToken = redisService.get(Constants.JWT_ACCESS_TOKEN_KEY, String.class);


        // 判断refreshToken是否存在：如果存在，则继续执行，否则该请求没有授权
        if (StringUtils.isEmpty(refreshToken) || StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(cacheAccessToken)) {
            authenticationEntryPoint.commence(request, response, new AuthenticationCredentialsNotFoundException(null));
            return;
        }

        // 从redis获取refreshToken(如果过期，则获取的Token为空)
        String cacheRefreshToken = redisService.get(Constants.JWT_REFRESH_TOKEN_KEY, String.class);

        // cacheRefreshToken不存在或过期，则需要重新登陆
        if (StringUtils.isEmpty(cacheRefreshToken) || JwtUtils.isTokenExpired(cacheRefreshToken)) {
            authenticationEntryPoint.commence(request, response, new CredentialsExpiredException(null));
            return;
        }

        // 【客户端refreshToken与服务端refreshToken不一致】 或者 【客户端accessToken与服务端accessToken不一致】
        if (!cacheRefreshToken.equals(refreshToken) || !cacheAccessToken.equals(accessToken)) {
            authenticationEntryPoint.commence(request, response, new InsufficientAuthenticationException("身份验证信息不足"));
            return;
        }

        // 根据token获取用户信息
        Claims claims = getClaims(cacheAccessToken);
        String account = claims.getSubject();;
        List<GrantedAuthority> authorities = null;
        // accessToken过期，需要重新创建accessToken
        if (JwtUtils.isTokenExpired(cacheAccessToken)) {
            // 获取数据库用户信息
            UserDetails userDetails = userService.loadUserByUsername(account);
            authorities = (List<GrantedAuthority>) userDetails.getAuthorities();
            // 创建新的token
            accessToken = JwtUtils.createAccessToken(userDetails);
            redisService.set(Constants.JWT_ACCESS_TOKEN_KEY, accessToken);
        } else {
            // cacheAccessToken没过期情况下
            authorities = JwtUtils.getGrantedAuthorities(claims);
        }




        // 保存到上下文
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(account, null, authorities));

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
