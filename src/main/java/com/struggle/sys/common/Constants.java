package com.struggle.sys.common;

public interface Constants {

    // 密钥
    String JWT_SECRET = "C*F-JaNdRgUkXn2r5u8xC*F-JaNdRgUkXn2r5u8xC*F-JaNdRgUkXn2r5u8xC*F-JaNdRgUkXn2r5u8xC*F-JaNdRgUkXn2r5u8xC*F-JaNdRgUkXn2r5u8x/A?D(G+KbPeShVmYq3s6v9y$B&E)H@McQfTjWnZr4u7w";

//    Long JWT_ACCESS_TOKEN_EXPIRE = 10 * 60L; // 10分钟
    Long JWT_ACCESS_TOKEN_EXPIRE = 60L; // 1分钟
    String JWT_ACCESS_TOKEN_KEY = "accessToken";

//    Long JWT_REFRESH_TOKEN_EXPIRE = 24 * 60 * 60 * 7L; // 7天
    Long JWT_REFRESH_TOKEN_EXPIRE = 60 * 60L; // 60分钟

    String JWT_BEARER = "Bearer ";
    String JWT_HEAD_ACCESS_TOKEN = "Authorization";
    String JWT_HEAD_REFRESH_TOKEN = "RefreshToken";

    String JWT_REFRESH_TOKEN_KEY = "refreshToken";

    String SEMICOLON_DELIMITER = ":";


}
