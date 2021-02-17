package com.struggle.sys.model;

import com.struggle.sys.util.tree.RootTreeNode;

public class MenuNode extends RootTreeNode {

    public MenuNode(Long id, Long parentId, String label) {
        super(id, parentId, label);
    }

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

    public String getEnname() {
        return enname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

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

    public void setRequireAuth(Boolean requireAuth) {
        this.requireAuth = requireAuth;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Byte getAvailable() {
        return available;
    }

    public void setAvailable(Byte available) {
        this.available = available;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }
}
