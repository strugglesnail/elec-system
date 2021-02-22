package com.struggle.sys.controller;

import com.struggle.sys.common.ServerResponse;
import com.struggle.sys.model.MenuNode;
import com.struggle.sys.model.TreeNode;
import com.struggle.sys.model.vo.RoleVo;
import com.struggle.sys.pojo.SysRole;
import com.struggle.sys.service.SysMenuService;
import com.struggle.sys.service.SysRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @auther strugglesnail
 * @date 2021/2/21 00:13
 * @desc 角色信息
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);


    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysMenuService sysMenuService;


    @GetMapping("/getRolePage")
    public ServerResponse getRolePage(RoleVo role) {
        return sysRoleService.getRolePage(role);
    }

    @GetMapping("/getRoleById")
    public ServerResponse getRolePage(Long roleId) {
        SysRole Role = sysRoleService.getRoleById(roleId);
        return ServerResponse.createBySuccess(Role);
    }

    @GetMapping("/getMenuByRoleId")
    public ServerResponse getMenuByRoleId(Long roleId) {
        Map<String, List<TreeNode>> roleMenuMap = sysMenuService.getRoleMenuMap(roleId);
        return ServerResponse.createBySuccess(roleMenuMap);
    }

    @PostMapping("/addRole")
    public ServerResponse addRole(@RequestBody SysRole role) {
         sysRoleService.addRole(role);
        return ServerResponse.createBySuccessMessage("保存成功!");
    }

    @PostMapping("/updateRole")
    public ServerResponse updateRole(@RequestBody SysRole role) {
         sysRoleService.updateRole(role);
        return ServerResponse.createBySuccessMessage("更新成功!");
    }
    // 更新角色菜单
    @PostMapping("/updateRoleMenu")
    public ServerResponse updateRoleMenu(@RequestParam(value = "newMenuIds", required = false) Long[] newMenusIds, Long roleId) {
        if (Objects.isNull(roleId)) {
            return ServerResponse.createByErrorMessage("参数为空!");
        }
         sysRoleService.updateRoleMenu(newMenusIds, roleId);
        return ServerResponse.createBySuccessMessage("更新成功!");
    }

    @GetMapping("/deleteRole")
    public ServerResponse deleteRole(Long roleId) {
         sysRoleService.deleteRole(roleId);
        return ServerResponse.createBySuccessMessage("删除成功!");
    }
}
