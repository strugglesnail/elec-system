package com.struggle.sys.service.design.updateRoleMenu;

import com.struggle.sys.mapper.SysRoleMapper;
import com.struggle.sys.model.RoleMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther strugglesnail
 * @date 2021/2/24 22:45
 * @desc oldMenuIds参数不为空情况
 */
public class NotEmptyMenuResolver implements MenuResolver {

    private static final Logger logger = LoggerFactory.getLogger(NotEmptyMenuResolver.class);


    private Long[] oldMenuIds;
    private SysRoleMapper sysRoleMapper;

    public NotEmptyMenuResolver(Long[] oldMenuIds, SysRoleMapper roleMapper) {
        this.oldMenuIds = oldMenuIds;
        this.sysRoleMapper = roleMapper;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean hasOldMenuIdsEmpty() {
        return oldMenuIds.length > 0;
    }

    @Override
    public void operateRoleMenu(List<RoleMenu> roleMenus, Long[] newMenuIds, Long roleId) {
        logger.info("oldMenuIds不为空");
        // 2、不为空, 则oldMenuIds与roleMenus求差集
        List<RoleMenu> matchMenus = roleMenus.stream().filter(menu -> !Arrays.stream(oldMenuIds).anyMatch(menuId -> menu.getMenuId().equals(menuId))).collect(Collectors.toList());
        // 1、差集为空说明roleMenus不变化, 新增newMenuIds
        if (CollectionUtils.isEmpty(matchMenus)) {
            logger.info("差集为空说明roleMenus不变化, 新增newMenuIds");
            for (int i = 0; i < newMenuIds.length; i++) {
                this.sysRoleMapper.saveRoleMenu(new RoleMenu(null, roleId, newMenuIds[i]));
            }
        } else {
            // 2、差集不为空
            // 1、差集大于newMenuIds，则先更新后删除
            if (matchMenus.size() > newMenuIds.length) {
                logger.info("差集大于newMenuIds，则先更新后删除");
                for (int i = 0; i < newMenuIds.length; i++) {
                    this.sysRoleMapper.updateRoleMenu(new RoleMenu(roleMenus.get(i).getId(), null, newMenuIds[i]));
                }
                for (int i = newMenuIds.length; i < matchMenus.size(); i++) {
                    this.sysRoleMapper.deleteRoleMenu(matchMenus.get(i).getId());
                }
            } else {
                logger.info("差集小于等于newMenuIds，则更新后新增");
                // 2、差集小于等于newMenuIds，则更新后新增
                for (int i = 0; i < matchMenus.size(); i++) {
                    this.sysRoleMapper.updateRoleMenu(new RoleMenu(roleMenus.get(i).getId(), null, newMenuIds[i]));
                }
                for (int i = matchMenus.size(); i < newMenuIds.length; i++) {
                    this.sysRoleMapper.saveRoleMenu(new RoleMenu(null, roleId, newMenuIds[i]));
                }
            }

        }
    }
}
