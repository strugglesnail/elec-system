package com.struggle.sys.service.design.updateRoleMenu;

import com.struggle.sys.mapper.SysRoleMapper;
import com.struggle.sys.model.RoleMenu;

import java.util.List;

/**
 * @auther strugglesnail
 * @date 2021/2/24 22:22
 * @desc 角色菜单解析器：对角色授权操作
 */
public interface MenuResolver {

    /**
     * 判断oldMenuIds为空
     * @return
     */
    boolean hasOldMenuIdsEmpty();

    /**
     * oldMenuIds：角色原先的变化菜单权限(参数)
     * @return
     */
//    Long[] getOldMenuIds();

    /**
     * 操作角色菜单表
     * @param roleMenus：角色原先的菜单权限(库)
     * @param newMenuIds：新增的菜单权限(参数)
     * @param roleId：角色id(参数)
     */
    void operateRoleMenu(List<RoleMenu> roleMenus, Long[] newMenuIds, Long roleId);

    default SysRoleMapper getRoleMapper() { return null; };
}
