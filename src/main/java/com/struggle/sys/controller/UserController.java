package com.struggle.sys.controller;
import java.util.Date;

import com.struggle.sys.common.Constants;
import com.struggle.sys.common.ServerResponse;
import com.struggle.sys.model.MenuNode;
import com.struggle.sys.pojo.SysUser;
import com.struggle.sys.service.SysMenuService;
import com.struggle.sys.service.SysUserService;
import com.struggle.sys.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @auther strugglesnail
 * @date 2021/1/20 22:12
 * @desc
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserService sysUserService;


    @GetMapping("/getMenuNodeTree")
    public List<MenuNode> getMenuNodeTree(Long userId) {
        return sysMenuService.getMenuNodeTree(userId);
    }

    @GetMapping("/info")
    public ServerResponse getInfo(String token) {
        token = token.replace(Constants.JWT_BEARER, "");
        Claims claims = JwtUtils.parseToken(token);
        SysUser user = sysUserService.getUserByUserId(Long.valueOf(claims.get("userId", Long.class)));
        return ServerResponse.createBySuccess(user);
    }
}
