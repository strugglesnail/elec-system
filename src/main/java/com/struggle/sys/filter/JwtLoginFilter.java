package com.struggle.sys.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.struggle.sys.common.TokenResponse;
//import com.struggle.sys.model.User;
import com.struggle.sys.security.CustomAuthenticationEntryPoint;
import com.struggle.sys.util.JwtUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @auther strugglesnail
 * @date 2021/1/25 23:01
 * @desc Jwt登录过滤器
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    private CustomAuthenticationEntryPoint authenticationEntryPoint = new CustomAuthenticationEntryPoint();



    public JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(authenticationManager);
    }


    // 登录认证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    }

    // 登录成功返回信息
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        String name = authResult.getName();
        String[] split = name.split(",");
        UserDetails userDetails = User.builder().username(split[1]).authorities(authorities).build();

        // 创建accessToken
        String accessToken = JwtUtils.createAccessToken(userDetails);

        // 创建refreshToken
        String refreshToken = JwtUtils.createRefreshToken(split[1]);

        // 存入redis
        ///////////
        ///////////

        // 启动定时任务（判断refreshToken超时时间刷新accessToken：可以第三方调用）

        // 退出操作


        // 响应登录信息
        JwtUtils.writerToJson(response, TokenResponse.createBySuccessMessage(accessToken, Long.valueOf(split[0])));
    }




    // 登录失败返回信息
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        authenticationEntryPoint.commence(request, response,  failed);
    }


}
