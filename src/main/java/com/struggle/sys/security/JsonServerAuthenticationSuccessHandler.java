package com.struggle.sys.security;

import com.struggle.sys.common.Constants;
import com.struggle.sys.common.TokenResponse;
import com.struggle.sys.pojo.SysUser;
import com.struggle.sys.service.RedisService;
import com.struggle.sys.service.SysUserService;
import com.struggle.sys.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Date;

/**
 * @Author WTF
 * @Date 2020/3/2 21:12
 * @Description
 */
@Component
public class JsonServerAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {


    @Autowired
    private RedisService redisService;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String name = authentication.getName();
        String[] split = name.split(Constants.SEMICOLON_DELIMITER);
        // 用户Id
        Long userId = Long.valueOf(split[0]);

        // 用户账号
        String account = name;

        // 创建用户信息
        UserDetails userDetails = User.builder().username(name).password("").authorities(authorities).build();

        // 创建accessToken
        String accessToken = JwtUtils.createAccessToken(userDetails);

        // 创建refreshToken
        String refreshToken = JwtUtils.createRefreshToken(account);

        // 存入redis
        redisService.set(JwtUtils.getAccessTokenKey(userId), accessToken);
        redisService.set(JwtUtils.getRefreshTokenKey(userId), refreshToken);

        // 更新用户登录时间
//        updateUserLoginTime(userId);
        // 保存到上下文
        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext));

        return JwtUtils.writerToJson(webFilterExchange.getExchange().getResponse(), TokenResponse.createBySuccessMessage(accessToken, refreshToken, userId));
    }

    // 更新用户登录时间
    private void updateUserLoginTime(Long userId) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setLastLoginTime(new Date());
        sysUserService.updateUser(user);
    }
}
