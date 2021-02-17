package com.struggle.sys.controller;
import java.util.Collection;
import java.util.Date;

import com.struggle.sys.common.Constants;
import com.struggle.sys.common.ServerResponse;
import com.struggle.sys.common.TokenResponse;
import com.struggle.sys.filter.JwtFilter;
import com.struggle.sys.model.MenuNode;
import com.struggle.sys.model.dto.UserMenuDTO;
import com.struggle.sys.pojo.SysUser;
import com.struggle.sys.service.RedisService;
import com.struggle.sys.service.SysMenuService;
import com.struggle.sys.service.SysUserService;
import com.struggle.sys.service.UserService;
import com.struggle.sys.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

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
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;


    @GetMapping("/getMenuNodeTree")
    public List<MenuNode> getMenuNodeTree(Long userId) {
        return sysMenuService.getMenuNodeTree(userId);
    }

    @GetMapping("/info")
    public ServerResponse getInfo(String token, HttpServletResponse response) {
        String newToken = response.getHeader(Constants.JWT_HEAD_ACCESS_TOKEN);
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


    @GetMapping("/checkToken")
    public TokenResponse checkToken(String accessToken, Long userId) {
        if (Objects.isNull(userId)) {
            return TokenResponse.createByErrorMessage("userId不为空!");
        }
        // 根据token获取用户信息
        Collection<? extends GrantedAuthority> authorities;
        // accessToken过期，需要重新创建accessToken
        if (JwtUtils.isTokenExpired(JwtUtils.removeBearer(accessToken))) {
             // 获取数据库用户信息
            UserDetails userDetails = userService.loadUserByUserId(userId);
            authorities = userDetails.getAuthorities();
            // 创建新的token
            accessToken = JwtUtils.createAccessToken(userDetails);
            redisService.set(Constants.JWT_ACCESS_TOKEN_KEY, accessToken);
            // 保存到上下文
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userId, null, authorities));

            String cacheRefreshToken = redisService.get(JwtUtils.getRefreshTokenKey(userId), String.class);
            return TokenResponse.createBySuccessMessage(accessToken, cacheRefreshToken, userId);
        }
        return null;

    }
}
