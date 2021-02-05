package com.struggle.sys.service;

import com.struggle.sys.mapper.SysUserMapper;
import com.struggle.sys.pojo.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @auther strugglesnail
 * @date 2021/1/23 14:45
 * @desc 用户接口服务
 */
@Service
public class SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;


    // 根据用户名称获取用户信息
    public SysUser getUserByUserName(String userName) {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(userName);
        return sysUserMapper.getOne(sysUser);
    }

    // 根据userId获取用户信息
    public SysUser getUserByUserId(Long userId) {
        return sysUserMapper.getById(userId);
    }
}
