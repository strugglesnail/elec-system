package com.struggle.sys.security;

import com.struggle.sys.common.Constants;
import com.struggle.sys.common.ServerResponse;
import com.struggle.sys.service.RedisService;
import com.struggle.sys.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author WTF
 * @Date 2020/2/18 10:48
 * @Description 登出成功回调
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private RedisService redisService;

    // 登出成功操作
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 清除token缓存
        redisService.delete(Constants.JWT_ACCESS_TOKEN_KEY);
        redisService.delete(Constants.JWT_REFRESH_TOKEN_KEY);
        JwtUtils.writerToJson(response, ServerResponse.createBySuccessMessage("注销成功!"));
    }
}
