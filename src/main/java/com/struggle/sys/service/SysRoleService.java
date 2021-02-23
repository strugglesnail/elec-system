package com.struggle.sys.service;

import com.google.common.collect.Lists;
import com.struggle.sys.common.ServerResponse;
import com.struggle.sys.mapper.SysRoleMapper;
import com.struggle.sys.model.RoleMenu;
import com.struggle.sys.model.vo.RoleVo;
import com.struggle.sys.pojo.SysRole;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @auther strugglesnail
 * @date 2021/3/21 00:06
 * @desc 角色接口服务
 */
@Service
public class SysRoleService {

    private static final Logger logger = LoggerFactory.getLogger(SysRoleService.class);



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
    @Transactional
    public void updateRoleMenu(Long[] oldMenuIds, Long[] newMenuIds, Long roleId) {
        // 新增菜单为空情况，则直接返回
        if (newMenuIds == null || newMenuIds.length == 0) {
            logger.info("新增菜单为空!");
            return;
        }
        List<RoleMenu> roleMenus = sysRoleMapper.getRoleMenuByRoleId(roleId);
        roleMenus = CollectionUtils.isEmpty(roleMenus) ? Lists.newArrayList() : roleMenus;

        // 判断oldMenuIds是否空
        // 1、为空则更新掉roleMenus
        if (oldMenuIds == null || oldMenuIds.length == 0) {

            // newMenuIds小于roleMenus，则更新后删除
            if (newMenuIds.length < roleMenus.size()) {
                // 覆盖更新
                for (int i = 0; i < newMenuIds.length; i++) {
                    sysRoleMapper.updateRoleMenu(new RoleMenu(roleMenus.get(i).getId(), null, newMenuIds[i]));
                }
                // roleMenus多余删除
                for (int i = newMenuIds.length; i < roleMenus.size(); i++) {
                    sysRoleMapper.deleteRoleMenu(roleMenus.get(i).getId());
                }
            } else {
                // newMenuIds大于roleMenus，则更新后新增
                // 更新掉roleMenus
                for (int i = 0; i < roleMenus.size(); i++) {
                    sysRoleMapper.updateRoleMenu(new RoleMenu(roleMenus.get(i).getId(), null, newMenuIds[i]));
                }
                // newMenuIds多余新增
                for (int i = roleMenus.size(); i < newMenuIds.length; i++) {
                    sysRoleMapper.saveRoleMenu(new RoleMenu(null, roleId, newMenuIds[i]));
                }
            }


        } else {
            // 2、不为空, 则oldMenuIds与roleMenus求差集
            List<RoleMenu> matchMenus = roleMenus.stream().filter(menu -> !Arrays.stream(oldMenuIds).anyMatch(menuId -> menu.getMenuId().equals(menuId))).collect(Collectors.toList());
            // 1、差集为空说明roleMenus不变化, 新增newMenuIds
            if (CollectionUtils.isEmpty(matchMenus)) {
                for (int i = 0; i < newMenuIds.length; i++) {
                    sysRoleMapper.saveRoleMenu(new RoleMenu(null, roleId, newMenuIds[i]));
                }
            } else {
                // 2、差集不为空
                // 1、差集大于newMenuIds，则先更新后删除
                if (matchMenus.size() > newMenuIds.length) {
                    for (int i = 0; i < newMenuIds.length; i++) {
                        sysRoleMapper.updateRoleMenu(new RoleMenu(roleMenus.get(i).getId(), null, newMenuIds[i]));
                    }
                    for (int i = newMenuIds.length; i < matchMenus.size(); i++) {
                        sysRoleMapper.deleteRoleMenu(matchMenus.get(i).getId());
                    }
                } else {
                    // 2、差集小于等于newMenuIds，则更新后新增
                    for (int i = 0; i < matchMenus.size(); i++) {
                        sysRoleMapper.updateRoleMenu(new RoleMenu(roleMenus.get(i).getId(), null, newMenuIds[i]));
                    }
                    for (int i = matchMenus.size(); i < newMenuIds.length; i++) {
                        sysRoleMapper.saveRoleMenu(new RoleMenu(null, roleId, newMenuIds[i]));
                    }
                }

            }




        }





//        // 如果新增的菜单数量大于roleMenus数量则把roleMenus更新掉，然后插入多余的新增菜单
//        if (newMenuIds.length > roleMenus.size()) {
//            // 更新掉roleMenus
//            for (int i = 0; i < roleMenus.size(); i++) {
//                RoleMenu roleMenu = new RoleMenu(roleMenus.get(i).getId(), null, newMenuIds[i]);
//                sysRoleMapper.updateRoleMenu(roleMenu);
//            }
//            // 插入多余的新增菜单
//            for (int i = roleMenus.size(); i < newMenuIds.length; i++) {
//                RoleMenu roleMenu = new RoleMenu(null, roleId, newMenuIds[i]);
//                sysRoleMapper.saveRoleMenu(roleMenu);
//            }
//        } else {
//            // 如果新增的菜单数量小于roleMenus数量则把roleMenus部分更新掉，然后删除多余的roleMenus
//            for (int i = 0; i < newMenuIds.length; i++) {
//                RoleMenu roleMenu = new RoleMenu(roleMenus.get(i).getId(), null, newMenuIds[i]);
//                sysRoleMapper.updateRoleMenu(roleMenu);
//            }
//            // 删除多余的roleMenus
//            for (int i = newMenuIds.length; i < roleMenus.size(); i++) {
//                sysRoleMapper.deleteRoleMenu(roleMenus.get(i).getId());
//            }
//        }



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
