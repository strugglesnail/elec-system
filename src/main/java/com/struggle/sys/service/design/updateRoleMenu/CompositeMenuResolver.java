package com.struggle.sys.service.design.updateRoleMenu;

import com.struggle.sys.mapper.SysRoleMapper;
import com.struggle.sys.model.RoleMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @auther strugglesnail
 * @date 2021/2/24 23:04
 * @desc
 */
public class CompositeMenuResolver implements MenuResolver {

    private List<MenuResolver> menuResolvers = new ArrayList<>(2);
    private SysRoleMapper sysRoleMapper;




    public CompositeMenuResolver(Long[] oldMenuIds, SysRoleMapper roleMapper) {
        this.sysRoleMapper = roleMapper;
        menuResolvers.add(new EmptyMenuResolver(oldMenuIds, sysRoleMapper));
        menuResolvers.add(new NotEmptyMenuResolver(oldMenuIds, sysRoleMapper));
    }

    @Override
    public final boolean hasOldMenuIdsEmpty() {
//        MenuResolver menuResolver = getMenuResolver();
//        if (Objects.isNull(menuResolver)) {
//            throw new IllegalStateException("没有找到菜单解析器!");
//        }
//        return menuResolver.hasOldMenuIdsEmpty();
        return false;
    }

    @Override
    public void operateRoleMenu(List<RoleMenu> roleMenus, Long[] newMenuIds, Long roleId) {
        MenuResolver menuResolver = getMenuResolver();
        if (Objects.isNull(menuResolver)) {
            throw new IllegalStateException("没有找到菜单解析器!");
        }
        menuResolver.operateRoleMenu(roleMenus, newMenuIds, roleId);
    }

    private MenuResolver getMenuResolver() {
        for (MenuResolver menuResolver : menuResolvers) {
            if (menuResolver.hasOldMenuIdsEmpty()){
                return menuResolver;
            }
        }
        return null;
    }

}
