package com.struggle.sys.common;

public enum TokenCode {
    SUCCESS(200, "SUCCESS"),
    ERROR(500, "ERROR"),
    NO_AUTHORITY(403, "没有权限访问"),
    UNAUTHORIZED (401, "没有授权"),
    LOGIN_EXPIRED (402, "登陆时间过长，请重新登陆");



    private final int code;

    private final String desc;

    TokenCode(int code, String desc) {
        this.code =code;
        this.desc = desc;
    }

    //get没有set因为是final
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }


}