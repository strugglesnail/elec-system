package com.struggle.sys.common;


import java.io.Serializable;

/**
 * JSON返回工具类
 * @param <T>
 */
public class TokenResponse<T> implements Serializable{
    private String msg;
    private int code;
    private String token;
    private Long userId;


    //构造方法全部私有

    private TokenResponse(int code, String msg) {
        this(code, msg, null);
    }

    private TokenResponse(int code, String msg, String token) {
        this(code, msg, token, null);
    }
    private TokenResponse(int code, String msg, String token, Long userId) {
        this.code = code;
        this.msg = msg;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public Long getUserId() {
        return userId;
    }

    //静态方法对外开放,返回一个成功的构造器,带msg信息
    public static <T> TokenResponse<T> createBySuccessMessage(String token, Long userId) {
        return new TokenResponse(TokenCode.SUCCESS.getCode(), TokenCode.SUCCESS.getDesc(), token, userId);

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
        return new TokenResponse(TokenCode.LOGIN_EXPIRED.getCode(), TokenCode.UNAUTHORIZED.getDesc());
    }


}