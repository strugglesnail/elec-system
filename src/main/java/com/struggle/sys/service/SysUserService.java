package com.struggle.sys.service;

import com.struggle.sys.common.ServerResponse;
import com.struggle.sys.mapper.SysRoleMapper;
import com.struggle.sys.mapper.SysUserMapper;
import com.struggle.sys.model.vo.UserVo;
import com.struggle.sys.pojo.SysRole;
import com.struggle.sys.pojo.SysUser;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther strugglesnail
 * @date 2021/1/23 14:45
 * @desc 用户接口服务
 */
@Service
public class SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;


    // 根据用户账号称获取用户信息
    public SysUser getUserByAccount(String account) {
        SysUser sysUser = new SysUser();
        sysUser.setAccount(account);
        return sysUserMapper.getOne(sysUser);
    }

    // 分页获取用户信息
    public ServerResponse getUserPage(UserVo user) {
        int limit = user.getPageSize();
        int offset = (user.getPageNo() - 1) * limit;
        RowBounds rowBounds = new RowBounds(offset, limit);
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(user, sysUser);
        List<SysUser> list = sysUserMapper.listLikePage(sysUser, rowBounds);
        Long count = sysUserMapper.countLike(sysUser);
        return ServerResponse.createBySuccessForTable(count, list, "成功!");
    }

    // 根据userId获取用户信息
    public SysUser getUserByUserId(Long userId) {
        return sysUserMapper.getById(userId);
    }

    // 根据userId获取角色
    public Map<String, List<SysRole>> getUserRoleById(Long userId) {
        List<SysRole> allRoles = sysRoleMapper.list(new SysRole());
        List<SysRole> userRoles = sysRoleMapper.getUserRoleByUserId(userId);
        Map<String, List<SysRole>> resultMap = new HashMap<>(2);
        resultMap.put("allRoles", allRoles);
        resultMap.put("userRoles", userRoles);
        return resultMap;
    }

    // 新增用户
    @Transactional
    public void addUser(SysUser user) {
        sysUserMapper.save(user);
    }
    // 修改用户
    @Transactional
    public void updateUser(SysUser user) {
        sysUserMapper.update(user);
    }
    // 删除用户
    @Transactional
    public void deleteUser(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setId(userId);
        sysUser.setStatus((byte) 1);
        sysUserMapper.update(sysUser);
    }
}
