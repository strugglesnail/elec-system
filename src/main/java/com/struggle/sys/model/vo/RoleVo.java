package com.struggle.sys.model.vo;

import com.struggle.sys.pojo.SysRole;

/**
 * @author strugglesnail
 * @date 2021/2/21
 * @desc
 */
public class RoleVo extends SysRole {

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
