package com.struggle.sys.service.design.updateRoleMenu;

import com.struggle.sys.model.RoleMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther strugglesnail
 * @date 2021/2/24 23:04
 * @desc
 */
public class CompositeMenuResolver implements MenuResolver {

    private static List<MenuResolver> menuResolvers = new ArrayList<>(2);
    static {
        menuResolvers.add(new EmptyMenuResolver(null));
        menuResolvers.add(new NotEmptyMenuResolver(null));
    }

    @Override
    public boolean hasOldMenuIdsEmpty() {
        return menuResolvers();
    }

    @Override
    public void operateRoleMenu(List<RoleMenu> roleMenus, Long[] newMenuIds, Long roleId) {

    }
}
