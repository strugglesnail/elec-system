package com.struggle.sys.util;

import com.struggle.sys.common.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;

import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

/**
 * @auther strugglesnail
 * @date 2021/2/3 21:54
 * @desc
 */
public class JwtUtils {

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
}
