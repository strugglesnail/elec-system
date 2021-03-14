package com.struggle.sys.controller;

import com.struggle.sys.common.Constants;
import com.struggle.sys.common.ServerResponse;
import com.struggle.sys.model.MenuNode;
import com.struggle.sys.model.dto.UserMenuDTO;
import com.struggle.sys.model.vo.UserVo;
import com.struggle.sys.pojo.SysRole;
import com.struggle.sys.pojo.SysUser;
import com.struggle.sys.service.SysMenuService;
import com.struggle.sys.service.SysUserService;
import com.struggle.sys.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @auther strugglesnail
 * @date 2021/1/20 22:12
 * @desc
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserService sysUserService;


    @GetMapping("/getMenuNodeTree")
    public List<MenuNode> getMenuNodeTree(Long userId) {
        return sysMenuService.getMenuNodeTree(userId);
    }

    @GetMapping("/info")
    public ServerResponse getInfo(String token, ServerHttpResponse response) {
        String newToken = response.getHeaders().getFirst(Constants.JWT_HEAD_ACCESS_TOKEN);
        if (StringUtils.hasText(newToken)) {
            token = newToken;
        }
        token = token.replace(Constants.JWT_BEARER, "");
        Claims claims = JwtUtils.parseToken(token);
        Long userId = Long.valueOf(claims.get("userId", Long.class));
        SysUser user = sysUserService.getUserByUserId(userId);
        List<MenuNode> menuList = sysMenuService.getMenuNodeTree(userId);
        UserMenuDTO userMenu = new UserMenuDTO();
        userMenu.setUsername(user.getUsername());
        userMenu.setAvatar(user.getAvatar());
        userMenu.setMenuList(menuList);
        return ServerResponse.createBySuccess(userMenu);
    }

    @GetMapping("/getUserPage")
    public ServerResponse getUserPage(UserVo user) {
        return sysUserService.getUserPage(user);
    }

    @GetMapping("/getUserById")
    public ServerResponse getUserPage(Long userId) {
        SysUser user = sysUserService.getUserByUserId(userId);
        return ServerResponse.createBySuccess(user);
    }

    @GetMapping("/getUserRoleById")
    public ServerResponse getUserRoleById(Long userId) {
        Map<String, List<SysRole>> rolesMap = sysUserService.getUserRoleById(userId);
        return ServerResponse.createBySuccess(rolesMap);
    }

    @PostMapping("/addUser")
    public ServerResponse addUser(@RequestBody SysUser user) {
         sysUserService.addUser(user);
        return ServerResponse.createBySuccessMessage("保存成功!");
    }

    @PostMapping("/updateUser")
    public ServerResponse updateUser(@RequestBody SysUser user) {
         sysUserService.updateUser(user);
        return ServerResponse.createBySuccessMessage("更新成功!");
    }

    @GetMapping("/deleteUser")
    public ServerResponse deleteUser(Long userId) {
         sysUserService.deleteUser(userId);
        return ServerResponse.createBySuccessMessage("删除成功!");
    }
}
