package com.struggle.sys.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.struggle.sys.common.Constants;
import com.struggle.sys.common.TokenResponse;
import com.struggle.sys.model.LoginUser;
import com.struggle.sys.security.LoginAuthenticationEntryPoint;
import com.struggle.sys.service.RedisService;
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

/**
 * @auther strugglesnail
 * @date 2021/1/25 23:01
 * @desc Jwt登录过滤器
 */
//@Component
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private RedisService redisService;

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
//        new LoginUser(split[1], "", (List<GrantedAuthority>) authorities);
        UserDetails userDetails = User.builder().username(split[1]).password("").authorities(authorities).build();


        // 创建accessToken
        String accessToken = JwtUtils.createAccessToken(userDetails);

        // 创建refreshToken
        String refreshToken = JwtUtils.createRefreshToken(split[1]);

        // 存入redis
        redisService.set(Constants.JWT_ACCESS_TOKEN_EXPIRE_KEY, accessToken, Constants.JWT_ACCESS_TOKEN_EXPIRE);
        redisService.set(Constants.JWT_REFRESH_TOKEN_EXPIRE_KEY, refreshToken, Constants.JWT_REFRESH_TOKEN_EXPIRE);

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
