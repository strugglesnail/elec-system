package com.struggle.sys.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.struggle.sys.common.TokenResponse;
import com.struggle.sys.model.LoginUser;
import com.struggle.sys.pojo.SysUser;
import com.struggle.sys.security.LoginAuthenticationEntryPoint;
import com.struggle.sys.service.RedisService;
import com.struggle.sys.service.SysUserService;
import com.struggle.sys.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Collection;
import java.util.Date;

/**
 * @auther strugglesnail
 * @date 2021/1/25 23:01
 * @desc Jwt登录过滤器
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private RedisService redisService;

    @Autowired
    private SysUserService sysUserService;

    private LoginAuthenticationEntryPoint authenticationEntryPoint = new LoginAuthenticationEntryPoint();



    public JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(authenticationManager);
    }

    // 登录认证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        LoginUser user = new ObjectMapper().readValue(request.getInputStream(), LoginUser.class);
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    }

    // 登录成功返回信息
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        String name = authResult.getName();
        String[] split = name.split(",");
        // 用户Id
        Long userId = Long.valueOf(split[0]);
        // 用户账号
        String account = split[1];

        UserDetails userDetails = User.builder().username(name).password("").authorities(authorities).build();

        // 创建accessToken
        String accessToken = JwtUtils.createAccessToken(userDetails);

        // 创建refreshToken
        String refreshToken = JwtUtils.createRefreshToken(account);

        // 存入redis
        redisService.set(JwtUtils.getAccessTokenKey(userId), accessToken);
        redisService.set(JwtUtils.getRefreshTokenKey(userId), refreshToken);

        // 更新用户登录时间
        updateUserLoginTime(userId);

        // 响应登录信息
        JwtUtils.writerToJson(response, TokenResponse.createBySuccessMessage(accessToken, refreshToken, userId));
    }

    private void updateUserLoginTime(Long userId) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setLastLoginTime(new Date());
        sysUserService.updateUser(user);
    }


    // 登录失败返回信息
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        authenticationEntryPoint.commence(request, response,  failed);
    }


}
