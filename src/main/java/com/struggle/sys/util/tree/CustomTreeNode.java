package com.struggle.sys.util.tree;

public class CustomTreeNode extends RootTreeNode {

    public CustomTreeNode(Long id, Long parentId, String label, String type) {
        super(id, parentId, label);
        this.type = type;

//        System.out.println(id + ": " + parentId + ": " + label + ": " + type);
    }

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "MenuNode{" +
                "type='" + type + '\'' +
                "id='" + getId() + '\'' +
                '}';
    }
}
