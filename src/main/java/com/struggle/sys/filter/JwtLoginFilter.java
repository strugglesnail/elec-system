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


    // 认证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    }

    // 成功返回信息
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        String name = authResult.getName();
        String[] split = name.split(",");
        String authorityStr = authorities.stream().map(auth -> auth.getAuthority()).collect(Collectors.joining(","));
        String jwt = createAccessToken(split, authorityStr);

        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(TokenResponse.createBySuccessMessage(jwt, Long.valueOf(split[0]))));
        writer.flush();
        writer.close();
    }

    private String createAccessToken(String[] split, String authorities) {
        return Jwts.builder()
                .claim("authorities", authorities)
                .setId(split[0])
                .setSubject(split[1])
                .setExpiration(new Date(System.currentTimeMillis() + 120*1000))
                .signWith(JwtUtils.getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 登录失败返回信息
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        authenticationEntryPoint.commence(request, response,  failed);
    }


}
