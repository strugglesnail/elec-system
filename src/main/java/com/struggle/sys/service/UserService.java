package com.struggle.sys.service;

import com.google.common.collect.Lists;
import com.struggle.sys.common.Constants;
import com.struggle.sys.mapper.SysRoleMapper;
import com.struggle.sys.model.dto.RoleMenuDTO;
import com.struggle.sys.pojo.SysRole;
import com.struggle.sys.pojo.SysUser;
import com.struggle.sys.security.ResourcesGrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @auther strugglesnail
 * @date 2021/1/23 14:43
 * @desc 用户的认证与授权
 */
@Service
public class UserService implements ReactiveUserDetailsService, ReactiveUserDetailsPasswordService {


    @Autowired
    private SysUserService sysUserService;


    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public Mono<UserDetails> findByUsername(String account) {
        // 根据用户名称获取用户信息
        SysUser sysUser = sysUserService.getUserByAccount(account);
        // 授权集合
        List<ResourcesGrantedAuthority> grantedAuthorities;

        if (Objects.nonNull(sysUser)) {
            // 根据用户Id获取角色信息
            List<RoleMenuDTO> roles = sysRoleMapper.getRoleMenuByUserId(sysUser.getId());
            // 角色名称加入到授权集合
            grantedAuthorities = roles.stream().map(role -> new ResourcesGrantedAuthority(role.getRoleEnname(), Arrays.asList(role.getUrls().split(",")))).collect(Collectors.toList());
        } else {
            throw new UsernameNotFoundException("");
        }
        UserDetails userDetails = User.builder().username(sysUser.getId() + Constants.SEMICOLON_DELIMITER + account).password(sysUser.getPassword()).authorities(grantedAuthorities).build();
        return Mono.just(userDetails);
    }


    @Override
    public Mono<UserDetails> updatePassword(UserDetails userDetails, String newPassword) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
        UserDetails admin = User.builder().username("admin").password(newPassword).authorities(grantedAuthorities).build();
        return Mono.just(admin);
    }



    // 加载用户信息
    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
        // 根据用户名称获取用户信息
        SysUser sysUser = sysUserService.getUserByUserId(userId);
        // 授权集合
        List<GrantedAuthority> grantedAuthorities = Lists.newArrayList();

        if (Objects.nonNull(sysUser)) {
            // 根据用户Id获取角色信息
            List<RoleMenuDTO> roles = sysRoleMapper.getRoleMenuByUserId(sysUser.getId());
            grantedAuthorities = roles.stream().map(role -> new ResourcesGrantedAuthority(role.getRoleEnname(), Arrays.asList(role.getUrls().split(",")))).collect(Collectors.toList());
        } else {
            sysUser = new SysUser("", "", "");
        }
        // 返回当前用户账号密码及角色信息
        return new User(sysUser.getId() + Constants.SEMICOLON_DELIMITER + sysUser.getAccount(), sysUser.getPassword(), grantedAuthorities);
    }
}
