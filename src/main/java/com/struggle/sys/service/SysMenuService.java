package com.struggle.sys.service;

import com.struggle.sys.mapper.SysMenuMapper;
import com.struggle.sys.model.MenuNode;
import com.struggle.sys.model.TreeNode;
import com.struggle.sys.model.dto.MenuRoleDTO;
import com.struggle.sys.pojo.SysMenu;
import com.struggle.sys.util.tree.TreeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    // 根据菜单Id获取菜单
    public SysMenu getMenuById(Long menuId) {
        return sysMenuMapper.getById(menuId);
    }


    // 获取指定用户菜单树
    public List<MenuNode> getMenuNodeTree() {
        List<SysMenu> userMenuList = sysMenuMapper.getMenuList();
        return getMenuNodes(userMenuList);
    }

    // 获取指定用户菜单树
    public List<MenuNode> getMenuNodeTree(Long userId) {
        List<SysMenu> userMenuList = sysMenuMapper.getUserMenuById(userId);
        return getMenuNodes(userMenuList);
    }




    // 获取指定用户菜单树
    public List<TreeNode> getTreeNodes() {
        List<SysMenu> userMenuList = sysMenuMapper.getMenuList();
        return getTreeNodes(userMenuList);
    }

    // 获取指定角色菜单树
    public List<TreeNode> getRoleMenuNodeTree(Long roleId) {
        List<TreeNode> roleMenuList = sysMenuMapper.getMenuByRoleId(roleId);
        return roleMenuList;
    }

    // 获取角色菜单
    public Map<String, List<TreeNode>> getRoleMenuMap(Long roleId) {
        List<TreeNode> allMenu = this.getTreeNodes();
        List<TreeNode> roleMenu = this.getRoleMenuNodeTree(roleId);
        Map<String, List<TreeNode>> resultMap = new HashMap<>(2);
        resultMap.put("allMenu", allMenu);
        resultMap.put("roleMenu", roleMenu);
        return resultMap;
    }

    private List<MenuNode> getMenuNodes(List<SysMenu> userMenuList) {
        List<MenuNode> menuNodeList = userMenuList.stream().map(m -> {
            MenuNode node = new MenuNode(m.getId(), m.getParentId(), m.getName());
            BeanUtils.copyProperties(m, node);
            return node;
        }).collect(Collectors.toList());
        return TreeUtils.getTreeList(menuNodeList, 0L);
    }

    private List<TreeNode> getTreeNodes(List<SysMenu> userMenuList) {
        List<TreeNode> menuNodeList = userMenuList.stream().map(m -> {
            TreeNode node = new TreeNode(m.getId(), m.getParentId(), m.getName());
//            BeanUtils.copyProperties(m, node);
            return node;
        }).collect(Collectors.toList());
        return TreeUtils.getTreeList(menuNodeList, 0L);
    }



    // 获取菜单对应的角色
    public List<MenuRoleDTO> getMenuWithRole() {
        return sysMenuMapper.getMenuWithRole();
    }


    // 保存菜单
    @Transactional
    public void saveMenu(SysMenu menu) {
        sysMenuMapper.save(menu);
    }
    // 更新菜单
    @Transactional
    public void updateMenu(SysMenu menu) {
        sysMenuMapper.update(menu);
    }

    // 删除菜单
    @Transactional
    public void delMenu(Long[] menuIds) {
        sysMenuMapper.deleteBatch(menuIds);
    }

}
