package com.struggle.sys.pojo;

import java.util.Date;

/**
 * 用户表
 */
public class SysUser {

    private Long id;

    // 用户名称
    private String username;

    //用户账号
    private String account;

    private String password;

    //头像
    private String avatar;

    //账号创建时间
    private Date createTime;

    //最后一次登录时间
    private Date lastLoginTime;

    //账号状态(0-删除 1-正常 2-锁定 3-异常)
    private Byte status;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取头像
     *
     * @return avatar - 头像
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 设置头像
     *
     * @param avatar 头像
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    /**
     * 获取账号创建时间
     *
     * @return create_time - 账号创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置账号创建时间
     *
     * @param createTime 账号创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取最后一次登录时间
     *
     * @return last_login_time - 最后一次登录时间
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * 设置最后一次登录时间
     *
     * @param lastLoginTime 最后一次登录时间
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * 获取账号状态(0-删除 1-正常 2-锁定 3-异常)
     *
     * @return status - 账号状态(0-删除 1-正常 2-锁定 3-异常)
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置账号状态(0-删除 1-正常 2-锁定 3-异常)
     *
     * @param status 账号状态(0-删除 1-正常 2-锁定 3-异常)
     */
    public void setStatus(Byte status) {
        this.status = status;
    }
}