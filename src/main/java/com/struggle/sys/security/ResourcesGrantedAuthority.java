package com.struggle.sys.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author strugglesnail
 * @date 2021/3/10
 * @desc 资源权限
 */
public class ResourcesGrantedAuthority implements GrantedAuthority {


    private final String role;
    private final List<String> urls;

    public ResourcesGrantedAuthority(String role, List<String> urls) {
        Assert.hasText(role, "A granted authority textual representation is required");
        this.role = role;
        this.urls = urls;
    }

    public List<String> getUrls() {
        return urls;
    }

    public String getAuthority() {
        return this.role;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj instanceof ResourcesGrantedAuthority ? this.role.equals(((ResourcesGrantedAuthority)obj).role) : false;
        }
    }

    public int hashCode() {
        return this.role.hashCode();
    }

    public String toString() {
        return this.role;
    }
}
