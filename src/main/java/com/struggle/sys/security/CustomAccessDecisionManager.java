package com.struggle.sys.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @Author WTF
 * @Date 2020/2/18 11:18
 * @Description
 */
@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {

        for (ConfigAttribute configAttribute : configAttributes) {
            String needRole = configAttribute.getAttribute();
            // URL没有注册到菜单的情况下
            if ("NO_REGISTERED_URL".equals(needRole)) {
                if (authentication instanceof AnonymousAuthenticationToken) {
                    throw new InsufficientAuthenticationException("未注册的URL!");
                } else {
                    return;
                }
            }
            // 获取用户登录的授权信息
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if ("ROLE_ANONYMOUS".equals(authority.getAuthority())) {
                    throw new InsufficientAuthenticationException("尚未登录，请先登录!");
                }
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }

        throw new InsufficientAuthenticationException("权限不足，请联系管理员!");

    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
