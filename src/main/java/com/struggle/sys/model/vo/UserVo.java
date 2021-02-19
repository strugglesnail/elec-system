package com.struggle.sys.model.vo;

import com.struggle.sys.pojo.SysUser;

/**
 * @author strugglesnail
 * @date 2021/2/19
 * @desc
 */
public class UserVo extends SysUser {

    private Integer pageNo;
    private Integer pageSize;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
