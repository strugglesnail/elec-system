package com.struggle.sys.model.dto;

import com.struggle.sys.pojo.SysMenu;
import com.struggle.sys.pojo.SysRole;

import java.util.List;

/**
 * @auther strugglesnail
 * @date 2021/1/23 13:28
 * @desc
 */
// 菜单包装类：包含多个角色（一个菜单对应多个角色）
public class SysMenuRoleDTO extends SysMenu {

    private List<SysRole> roles;

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }
}
