package com.struggle.sys.util.tree;

import java.util.List;

/**
 * 树节点
 */
public class RootTreeNode<T> {

    private Long id;

    private Long parentId;

    private String label;

    private List<T> children;

    public RootTreeNode() {
    }

    public RootTreeNode(Long id, Long parentId, String label) {
        this.id = id;
        this.parentId = parentId;
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "RootTreeNode{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", label='" + label + '\'' +
                ", children=" + children +
                '}';
    }
}
