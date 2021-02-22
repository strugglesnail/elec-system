package com.struggle.sys.model;

/**
 * @author strugglesnail
 * @date 2021/2/22
 * @desc
 */
public class RoleMenu {

    public RoleMenu() {
    }

    public RoleMenu(Long id, Long roleId, Long menuId) {
        this.id = id;
        this.roleId = roleId;
        this.menuId = menuId;
    }

    private Long id;
    private Long roleId;
    private Long menuId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
