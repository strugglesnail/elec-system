package com.struggle.sys.common;

public interface Constants {

    String JWT_SECRET = "C*F-JaNdRgUkXn2r5u8xC*F-JaNdRgUkXn2r5u8xC*F-JaNdRgUkXn2r5u8xC*F-JaNdRgUkXn2r5u8xC*F-JaNdRgUkXn2r5u8xC*F-JaNdRgUkXn2r5u8x/A?D(G+KbPeShVmYq3s6v9y$B&E)H@McQfTjWnZr4u7w";

    Long JWT_ACCESS_TOKEN_EXPIRE = 10 * 60L; // 10分钟
    String JWT_ACCESS_TOKEN_REDIS_KEY = "accessToken";

    Long JWT_REFRESH_TOKEN_EXPIRE = 24 * 60 * 60 * 7L; // 7天

    String JWT_REFRESH_TOKEN_REDIS_KEY = "refreshToken";



    String GET_SUCCESS_MSG = "获取成功！";
    String SAVE_SUCCESS_MSG = "保存成功！";
    String UPDATE_SUCCESS_MSG = "更新成功！";
    String[] OPTION_MSG = {"部门编码已存在！", "部门名称已存在！", "部门编码/部门名称已存在"};

    // 授权码key
    String GIT_HUB_CODE = "gitHubKey";

}
