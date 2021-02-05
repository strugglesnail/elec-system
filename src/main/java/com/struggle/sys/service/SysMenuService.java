package com.struggle.sys.service;

import com.struggle.sys.mapper.SysMenuMapper;
import com.struggle.sys.model.MenuNode;
import com.struggle.sys.model.dto.SysMenuRoleDTO;
import com.struggle.sys.pojo.SysMenu;
import com.struggle.sys.util.tree.TreeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @auther strugglesnail
 * @date 2021/1/20 21:52
 * @desc Vue菜单接口服务
 */
@Service
public class SysMenuService {


    @Autowired
    private SysMenuMapper sysMenuMapper;

    // 获取用户菜单
    public List<SysMenu> getUserMenuById(Long userId) {
        return sysMenuMapper.getUserMenuById(userId);
    }


    // 获取指定用户菜单树
    public List<MenuNode> getMenuNodeTree(Long userId) {
        List<SysMenu> userMenuList = sysMenuMapper.getUserMenuById(userId);
        List<MenuNode> menuNodeList = userMenuList.stream().map(m -> {
            MenuNode node = new MenuNode(m.getId(), m.getParentId(), m.getName());
            BeanUtils.copyProperties(m, node);
            return node;
        }).collect(Collectors.toList());
        return TreeUtils.getTreeList(menuNodeList, 0L);
    }

    // 获取菜单对应的角色
    public List<SysMenuRoleDTO> getMenuWithRole() {
        return sysMenuMapper.getMenuWithRole();
    }

}
