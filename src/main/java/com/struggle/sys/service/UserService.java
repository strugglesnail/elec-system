package com.struggle.sys.service;

import com.google.common.collect.Lists;
import com.struggle.sys.mapper.SysRoleMapper;
import com.struggle.sys.pojo.SysRole;
import com.struggle.sys.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @auther strugglesnail
 * @date 2021/1/23 14:43
 * @desc 用户的认证与授权
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;


    @Autowired
    private SysRoleMapper sysRoleMapper;


    // 加载用户信息
    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        // 根据用户名称获取用户信息
        SysUser sysUser = sysUserService.getUserByAccount(account);
        // 授权集合
        List<GrantedAuthority> grantedAuthorities = Lists.newArrayList();

        if (Objects.nonNull(sysUser)) {
            // 根据用户Id获取角色信息
            List<SysRole> roles = sysRoleMapper.getUserRoleByUserId(sysUser.getId());
            for (SysRole role : roles) {
                // 角色名称加入到授权集合
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
            }
        } else {
            throw new UsernameNotFoundException("");
        }
        // 返回当前用户账号密码及角色信息
        return new User(sysUser.getId() + "," + sysUser.getAccount(), sysUser.getPassword(), grantedAuthorities);
    }

    // 加载用户信息
    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
        // 根据用户名称获取用户信息
        SysUser sysUser = sysUserService.getUserByUserId(userId);
        // 授权集合
        List<GrantedAuthority> grantedAuthorities = Lists.newArrayList();

        if (Objects.nonNull(sysUser)) {
            // 根据用户Id获取角色信息
            List<SysRole> roles = sysRoleMapper.getUserRoleByUserId(sysUser.getId());
            for (SysRole role : roles) {
                // 角色名称加入到授权集合
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
            }
        } else {
            sysUser = new SysUser("", "", "");
        }
        // 返回当前用户账号密码及角色信息
        return new User(sysUser.getId() + "," + sysUser.getAccount(), sysUser.getPassword(), grantedAuthorities);
    }
}
