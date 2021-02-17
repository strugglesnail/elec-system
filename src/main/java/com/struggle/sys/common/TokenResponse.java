package com.struggle.sys.common;


import java.io.Serializable;

/**
 * JSON返回工具类
 * @param <T>
 */
public class TokenResponse<T> implements Serializable{
    private String msg;
    private int code;
    private String accessToken;
    private String refreshToken;
    private Long userId;


    //构造方法全部私有

    private TokenResponse(int code, String msg) {
        this(code, msg, null, null);
    }

    private TokenResponse(int code, String msg, String accessToken, String refreshToken) {
        this(code, msg, accessToken, refreshToken, null);
    }
    private TokenResponse(int code, String msg, String accessToken, String refreshToken, Long userId) {
        this.code = code;
        this.msg = msg;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.userId = userId;
    }

    public boolean isSuccess() {
        return this.code == TokenCode.SUCCESS.getCode();
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getUserId() {
        return userId;
    }

    //静态方法对外开放,返回一个成功的构造器,带msg信息
    public static <T> TokenResponse<T> createBySuccessMessage(String accessToken, String refreshToken, Long userId) {
        return new TokenResponse(TokenCode.SUCCESS.getCode(), TokenCode.SUCCESS.getDesc(), accessToken, refreshToken, userId);

    }
    public static <T> TokenResponse<T> createByErrorMessage(String msg) {
        return new TokenResponse(TokenCode.ERROR.getCode(), msg);
    }


    // 没有权限状态
    public static <T> TokenResponse<T> createByNoAuthority() {
        return new TokenResponse(TokenCode.NO_AUTHORITY.getCode(), TokenCode.NO_AUTHORITY.getDesc());
    }

    // 没有授权状态
    public static <T> TokenResponse<T> createByUnauthorized() {
        return new TokenResponse(TokenCode.UNAUTHORIZED.getCode(), TokenCode.UNAUTHORIZED.getDesc());
    }
    // 没有授权状态
    public static <T> TokenResponse<T> createByUnauthorized(String msg) {
        return new TokenResponse(TokenCode.UNAUTHORIZED.getCode(), msg);
    }

    // 登陆超时
    public static <T> TokenResponse<T> createByLoginExpired() {
        return new TokenResponse(TokenCode.LOGIN_EXPIRED.getCode(), TokenCode.LOGIN_EXPIRED.getDesc());
    }

    // 未登录
    public static <T> TokenResponse<T> createByNoLogin() {
        return new TokenResponse(TokenCode.NO_LOGIN.getCode(), TokenCode.NO_LOGIN.getDesc());
    }


}