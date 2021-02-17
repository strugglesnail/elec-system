package com.struggle.sys.model.dto;

import com.struggle.sys.model.MenuNode;

import java.util.List;

/**
 * @auther strugglesnail
 * @date 2021/2/15 13:01
 * @desc 用户菜单
 */
public class UserMenuDTO {


    // 用户名称
    private String username;

    //头像
    private String avatar;

    // 用户菜单
    List<MenuNode> menuList;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<MenuNode> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuNode> menuList) {
        this.menuList = menuList;
    }
}
