package com.struggle.sys.model.dto;

/**
 * @author strugglesnail
 * @date 2021/2/22
 * @desc 角色菜单
 */
public class RoleMenuDTO {

    // 角色名称
    private String roleName;
    // 角色英文名称
    private String roleEnname;
    // 菜单资源Url
    private String urls;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleEnname() {
        return roleEnname;
    }

    public void setRoleEnname(String roleEnname) {
        this.roleEnname = roleEnname;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }
}
