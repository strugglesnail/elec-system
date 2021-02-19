package com.struggle.sys.controller;

import com.struggle.sys.common.ServerResponse;
import com.struggle.sys.model.MenuNode;
import com.struggle.sys.pojo.SysMenu;
import com.struggle.sys.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/saveMenu")
    public ServerResponse saveMenu(@RequestBody SysMenu menu) {
        sysMenuService.saveMenu(menu);
        return ServerResponse.createBySuccessMessage("保存成功!");
    }

    @PostMapping("/updateMenu")
    public ServerResponse updateMenu(@RequestBody SysMenu menu) {
        sysMenuService.updateMenu(menu);
        return ServerResponse.createBySuccessMessage("更新成功!");
    }

    @PostMapping("/deleteMenu")
    public ServerResponse deleteMenu(@RequestParam("menuIds") Long[] menuIds) {
        sysMenuService.delMenu(menuIds);
        return ServerResponse.createBySuccessMessage("删除成功!");
    }
}
