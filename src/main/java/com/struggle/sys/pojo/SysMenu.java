package com.struggle.sys.pojo;

public class SysMenu {
    private Long id;

    //父ID
    private Long parentId;

    //资源名称
    private String name;

    private String enname;

    //Vue组件
    private String component;

    //是否保持活性
    private Boolean keepAlive;

    private String path;

    //资源URL
    private String url;

    // 跳转路径
    private String redirect;

    //图标
    private String icon;

    private Boolean requireAuth;

    //资源类型 2: button 1: menu
    private Boolean type;

    //权限描述
    private String description;

    //是否可见 0-不可见 1-可见
    private Byte available;

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
     * 获取父ID
     *
     * @return parent_id - 父ID
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置父ID
     *
     * @param parentId 父ID
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取资源名称
     *
     * @return name - 资源名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置资源名称
     *
     * @param name 资源名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return enname
     */
    public String getEnname() {
        return enname;
    }

    /**
     * @param enname
     */
    public void setEnname(String enname) {
        this.enname = enname;
    }

    /**
     * 获取Vue组件
     *
     * @return component - Vue组件
     */
    public String getComponent() {
        return component;
    }

    /**
     * 设置Vue组件
     *
     * @param component Vue组件
     */
    public void setComponent(String component) {
        this.component = component;
    }

    /**
     * 获取是否保持活性
     *
     * @return keepAlive - 是否保持活性
     */
    public Boolean getKeepAlive() {
        return keepAlive;
    }

    /**
     * 设置是否保持活性
     *
     * @param keepAlive 是否保持活性
     */
    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    /**
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取资源URL
     *
     * @return url - 资源URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置资源URL
     *
     * @param url 资源URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getRequireAuth() {
        return requireAuth;
    }

    /**
     * @param requireAuth
     */
    public void setRequireAuth(Boolean requireAuth) {
        this.requireAuth = requireAuth;
    }

    /**
     * 获取资源类型 2: button 1: menu
     *
     * @return type - 资源类型 2: button 1: menu
     */
    public Boolean getType() {
        return type;
    }

    /**
     * 设置资源类型 2: button 1: menu
     *
     * @param type 资源类型 2: button 1: menu
     */
    public void setType(Boolean type) {
        this.type = type;
    }

    /**
     * 获取权限描述
     *
     * @return description - 权限描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置权限描述
     *
     * @param description 权限描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取是否可见 0-不可见 1-可见
     *
     * @return available - 是否可见 0-不可见 1-可见
     */
    public Byte getAvailable() {
        return available;
    }

    /**
     * 设置是否可见 0-不可见 1-可见
     *
     * @param available 是否可见 0-不可见 1-可见
     */
    public void setAvailable(Byte available) {
        this.available = available;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    @Override
    public String toString() {
        return "SysMenu{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", enname='" + enname + '\'' +
                ", component='" + component + '\'' +
                ", keepAlive=" + keepAlive +
                ", path='" + path + '\'' +
                ", url='" + url + '\'' +
                ", icon='" + icon + '\'' +
                ", requireAuth=" + requireAuth +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", available=" + available +
                '}';
    }
}