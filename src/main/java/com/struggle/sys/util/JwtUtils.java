package com.struggle.sys.util;

import com.alibaba.fastjson.JSON;
import com.struggle.sys.common.Constants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @auther strugglesnail
 * @date 2021/2/3 21:54
 * @desc
 */
public class JwtUtils {


    // 创建Token
    public static String createAccessToken(UserDetails userDetails) {
        String authorities = userDetails.getAuthorities().stream().map(auth -> auth.getAuthority()).collect(Collectors.joining(","));
        return Jwts.builder()
                .claim("authorities", authorities)
//                .setId(split[0])
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + Constants.JWT_ACCESS_TOKEN))
                .signWith(JwtUtils.getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public static String createRefreshToken(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Constants.JWT_REFRESH_TOKEN))
                .compact();
    }


    // 解析Token获取用户信息
    public static Claims parseToken(String token) throws CredentialsExpiredException {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(getSecretKey()).
                            parseClaimsJws(token.replace("Bearer", "")).getBody();
        } catch (ExpiredJwtException ejx) {
            ejx.printStackTrace();
            throw new CredentialsExpiredException("Token Time Out!");
        } catch (MalformedJwtException mje) {
            mje.printStackTrace();
            throw new InsufficientAuthenticationException("Token Format Error!");
        }
        return claims;
    }

    public static SecretKey getSecretKey() {
        byte[] bytes = DatatypeConverter.parseBase64Binary(Constants.JWT_SECRET);
        SecretKey secretKey = Keys.hmacShaKeyFor(bytes);
        return secretKey;
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
