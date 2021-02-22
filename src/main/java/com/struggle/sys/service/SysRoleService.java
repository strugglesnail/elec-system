package com.struggle.sys.service;

import com.google.common.collect.Lists;
import com.struggle.sys.common.ServerResponse;
import com.struggle.sys.mapper.SysRoleMapper;
import com.struggle.sys.model.RoleMenu;
import com.struggle.sys.model.vo.RoleVo;
import com.struggle.sys.pojo.SysRole;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
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

    // 更新角色授权
    public void updateRoleMenu(Long[] newMenuIds, Long roleId) {
        // 新增菜单为空情况，则直接返回
        if (newMenuIds.length == 0) {
            return;
        }
        List<RoleMenu> roleMenus = sysRoleMapper.getRoleMenuByRoleId(roleId);
        roleMenus = CollectionUtils.isEmpty(roleMenus) ? Lists.newArrayList() : roleMenus;

        // 如果新增的菜单数量大于roleMenus数量则把roleMenus更新掉，然后插入多余的新增菜单
        if (newMenuIds.length > roleMenus.size()) {
            // 更新掉roleMenus
            for (int i = 0; i < roleMenus.size(); i++) {
                RoleMenu roleMenu = new RoleMenu(roleMenus.get(i).getId(), null, newMenuIds[i]);
                sysRoleMapper.updateRoleMenu(roleMenu);
            }
            // 插入多余的新增菜单
            for (int i = roleMenus.size(); i < newMenuIds.length; i++) {
                RoleMenu roleMenu = new RoleMenu(null, roleId, newMenuIds[i]);
                sysRoleMapper.saveRoleMenu(roleMenu);
            }
        } else {
            // 如果新增的菜单数量小于roleMenus数量则把roleMenus部分更新掉，然后删除多余的roleMenus
            for (int i = 0; i < newMenuIds.length; i++) {
                RoleMenu roleMenu = new RoleMenu(roleMenus.get(i).getId(), null, newMenuIds[i]);
                sysRoleMapper.updateRoleMenu(roleMenu);
            }
            // 删除多余的roleMenus
            for (int i = newMenuIds.length; i < roleMenus.size(); i++) {
                sysRoleMapper.deleteRoleMenu(roleMenus.get(i).getId());
            }
        }



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
