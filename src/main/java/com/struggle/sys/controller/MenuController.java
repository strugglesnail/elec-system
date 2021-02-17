package com.struggle.sys.controller;

import com.struggle.sys.common.ServerResponse;
import com.struggle.sys.model.MenuNode;
import com.struggle.sys.pojo.SysMenu;
import com.struggle.sys.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @auther strugglesnail
 * @date 2021/2/17 08:52
 * @desc
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping("/menuList")
    public ServerResponse getMenuList() {
        List<MenuNode> menuList = sysMenuService.getMenuNodeTree();
        return ServerResponse.createBySuccess(menuList);
    }

    @GetMapping("/getMenuById")
    public ServerResponse getMenuById(Long menuId) {
        SysMenu menu = sysMenuService.getMenuById(menuId);
        return ServerResponse.createBySuccess(menu);
    }
}
