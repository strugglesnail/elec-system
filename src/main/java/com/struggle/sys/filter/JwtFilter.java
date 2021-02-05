package com.struggle.sys.filter;

import com.struggle.sys.security.CustomAuthenticationEntryPoint;
import com.struggle.sys.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @auther strugglesnail
 * @date 2021/1/25 23:21
 * @desc
 */
public class JwtFilter extends GenericFilterBean {

    private CustomAuthenticationEntryPoint authenticationEntryPoint = new CustomAuthenticationEntryPoint();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


//        if (SecurityContextHolder.getContext().getAuthentication() != null) {
//            filterChain.doFilter(request, servletResponse);
//            return;
//        }
        String author = request.getHeader("Authorization");
        if (StringUtils.hasText(author) && author.startsWith("Bearer")) {
            Claims claims = null;
            try {
                // 获取当前用户信息
                claims = getClaims(author);
            } catch (CredentialsExpiredException e) {
                authenticationEntryPoint.commence(request, response, e);
                return;
            } catch (InsufficientAuthenticationException e) {
                authenticationEntryPoint.commence(request, response, e);
                return;
            }

            // 获取当前登录的用户
            String username = claims.getSubject();

            // 获取当前用户权限
            List<GrantedAuthority> authorities =
                    AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));

            // 保存到上下文
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, authorities));
        }

        filterChain.doFilter(request, servletResponse);
    }

    // 解析Token获取用户信息
    private Claims getClaims(String token) throws CredentialsExpiredException {
        Claims claims = null;
        token = token.replace("Bearer", "");
        try {
            claims = JwtUtils.parseToken(token);
        } catch (ExpiredJwtException e) {
            throw new CredentialsExpiredException("Token Time Out!");
        } catch (MalformedJwtException e) {
            throw new InsufficientAuthenticationException("Token Format Error!");
        }
        return claims;
    }
}
