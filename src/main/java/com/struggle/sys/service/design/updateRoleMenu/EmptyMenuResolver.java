package com.struggle.sys.service.design.updateRoleMenu;

import com.struggle.sys.mapper.SysRoleMapper;
import com.struggle.sys.model.RoleMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @auther strugglesnail
 * @date 2021/2/24 22:38
 * @desc
 */
public class EmptyMenuResolver implements MenuResolver {

    private static final Logger logger = LoggerFactory.getLogger(EmptyMenuResolver.class);

    // 角色原先的变化菜单权限(参数)
    private Long[] oldMenuIds;
    private SysRoleMapper sysRoleMapper;

    public EmptyMenuResolver(Long[] oldMenuIds, SysRoleMapper roleMapper) {
        this.oldMenuIds = oldMenuIds;
        this.sysRoleMapper = roleMapper;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean hasOldMenuIdsEmpty() {
        return oldMenuIds.length == 0;
    }

    @Override
    public void operateRoleMenu(List<RoleMenu> roleMenus, Long[] newMenuIds, Long roleId) {
        // newMenuIds小于roleMenus，则更新后删除
        if (newMenuIds.length < roleMenus.size()) {
            logger.info("newMenuIds小于roleMenus，则更新后删除");
            // 覆盖更新
            for (int i = 0; i < newMenuIds.length; i++) {
                this.sysRoleMapper.updateRoleMenu(new RoleMenu(roleMenus.get(i).getId(), null, newMenuIds[i]));
            }
            // roleMenus多余删除
            for (int i = newMenuIds.length; i < roleMenus.size(); i++) {
                this.sysRoleMapper.deleteRoleMenu(roleMenus.get(i).getId());
            }
        } else {
            logger.info("newMenuIds大于roleMenus，则更新后新增");
            // newMenuIds大于roleMenus，则更新后新增
            // 更新掉roleMenus
            for (int i = 0; i < roleMenus.size(); i++) {
                this.sysRoleMapper.updateRoleMenu(new RoleMenu(roleMenus.get(i).getId(), null, newMenuIds[i]));
            }
            // newMenuIds多余新增
            for (int i = roleMenus.size(); i < newMenuIds.length; i++) {
                this.sysRoleMapper.saveRoleMenu(new RoleMenu(null, roleId, newMenuIds[i]));
            }
        }
    }
}
