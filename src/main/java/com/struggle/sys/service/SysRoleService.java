package com.struggle.sys.service;

import com.struggle.sys.common.ServerResponse;
import com.struggle.sys.mapper.SysRoleMapper;
import com.struggle.sys.model.vo.RoleVo;
import com.struggle.sys.pojo.SysRole;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @auther strugglesnail
 * @date 2021/3/21 00:06
 * @desc 角色接口服务
 */
@Service
public class SysRoleService {


    @Autowired
    private SysRoleMapper sysRoleMapper;




    // 分页获取角色信息
    public ServerResponse getRolePage(RoleVo role) {
        int limit = role.getPageSize();
        int offset = (role.getPageNo() - 1) * limit;
        RowBounds rowBounds = new RowBounds(offset, limit);
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(role, sysRole);
        List<SysRole> list = sysRoleMapper.listLikePage(sysRole, rowBounds);
        Long count = sysRoleMapper.countLike(sysRole);
        return ServerResponse.createBySuccessForTable(count, list, "成功!");
    }

    // 根据RoleId获取角色信息
    public SysRole getRoleById(Long roleId) {
        return sysRoleMapper.getById(roleId);
    }



    // 新增角色
    @Transactional
    public void addRole(SysRole role) {
        sysRoleMapper.save(role);
    }
    // 修改角色
    @Transactional
    public void updateRole(SysRole role) {
        sysRoleMapper.update(role);
    }
    // 删除角色
    @Transactional
    public void deleteRole(Long roleId) {
        sysRoleMapper.delete(roleId);
    }
}
