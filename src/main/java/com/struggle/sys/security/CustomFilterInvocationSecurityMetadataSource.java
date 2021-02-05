package com.struggle.sys.security;

import com.struggle.sys.model.dto.SysMenuRoleDTO;
import com.struggle.sys.pojo.SysRole;
import com.struggle.sys.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

/**
 * @Author WTF
 * @Date 2021/1/23 13:00
 * @Description 主要是根据用户传来的请求地址，获取请求需要的角色
 */
@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private SysMenuService sysMenuService;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        // 获取菜单对应的角色
        List<SysMenuRoleDTO> permissions = sysMenuService.getMenuWithRole();
        for (SysMenuRoleDTO perm : permissions) {
            if (StringUtils.isEmpty(perm.getUrl())) continue;
            if (antPathMatcher.match(perm.getUrl(), requestUrl)) {
                System.out.println("11: " + perm);
                List<SysRole> roles = perm.getRoles();
                String[] str = new String[roles.size()];
                for (int i = 0; i < roles.size(); i++) {
                    str[i] = roles.get(i).getName();
                }
                return SecurityConfig.createList(str);
            }
        }
        return SecurityConfig.createList("NO_REGISTERED_URL");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
