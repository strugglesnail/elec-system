package com.struggle.sys.util;

import com.alibaba.fastjson.JSON;
import com.struggle.sys.common.Constants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @auther strugglesnail
 * @date 2021/2/3 21:54
 * @desc
 */
public class JwtUtils {


    // 创建accessToken
    public static String createAccessToken(UserDetails userDetails) {
        String[] userInfo = userDetails.getUsername().split(",");
        Long userId = Long.valueOf(userInfo[0]);
        String account = userInfo[1];
                String authorities = userDetails.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.joining(","));
        return Constants.JWT_BEARER + Jwts.builder()
                .claim("authorities", authorities)
                .claim("userId", userId)
//                .setId(split[0])
                .setSubject(account)
                .setExpiration(new Date(System.currentTimeMillis() + Constants.JWT_ACCESS_TOKEN_EXPIRE * 1000))
                .signWith(JwtUtils.getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    // 创建refreshToken
    public static String createRefreshToken(String account) {
        return Jwts.builder().setSubject(account).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constants.JWT_REFRESH_TOKEN_EXPIRE * 1000))
                .compact();
    }


    // 解析Token获取用户信息
    public static Claims parseToken(String token) throws CredentialsExpiredException {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(getSecretKey()).
                            parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException ejx) {
            ejx.printStackTrace();
            throw new CredentialsExpiredException("Token Time Out!");
        } catch (MalformedJwtException mje) {
            mje.printStackTrace();
            throw new InsufficientAuthenticationException("Token Format Error!");
        }
        return claims;
    }

    // 检查Token是否已过期
    public static Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // 从Token中检索到期日期
    public static Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // 从Token中获取指定的信息：Claims::getExpiration
    public static <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = parseToken(token);
        return claimsResolver.apply(claims);
    }

    // 获取密钥
    public static SecretKey getSecretKey() {
        byte[] bytes = DatatypeConverter.parseBase64Binary(Constants.JWT_SECRET);
        SecretKey secretKey = Keys.hmacShaKeyFor(bytes);
        return secretKey;
    }

    public static UserDetails getUserDetails(String account, String password, Collection<GrantedAuthority> authorities) {
        return User.builder().username(account).password(password).authorities(authorities).build();
    }

    // 获取用户权限信息+
    public static List<GrantedAuthority> getGrantedAuthorities(Claims claims) {
        return AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
    }

    // 响应数据
    public static void writerToJson(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(JSON.toJSONString(obj));
        writer.flush();
        writer.close();
    }
}
