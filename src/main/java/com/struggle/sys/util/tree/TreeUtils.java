package com.struggle.sys.util.tree;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 树节点工具类：递归调用
 */
public class TreeUtils {


    public static <T extends RootTreeNode> List<T> getTreeList(List<T> nodeList, Long id) {
        return nodeList.stream().filter(node -> id.equals(node.getParentId()))
                .map(node -> getTreeNode(nodeList, node)).collect(Collectors.toList());
    }

    private static <T extends RootTreeNode> T getTreeNode(List<T> nodeList, T node) {
        List<?> childrenNode = nodeList.stream().filter(n -> node.getId().equals(n.getParentId()))
                .map(n -> getTreeNode(nodeList, n)).collect(Collectors.toList());
        node.setChildren(childrenNode);
        return node;
    }

    public static void main(String[] args) {
        test01();
    }

    private static void test01() {
        List<CustomTreeNode> nodeList = new ArrayList<>();
        CustomTreeNode node1 = new CustomTreeNode(1L, 0L, "父", "1");
        CustomTreeNode node2 = new CustomTreeNode(2L, 1L, "子1", "1");
        CustomTreeNode node3 = new CustomTreeNode(3L, 1L, "子2", "1");
        CustomTreeNode node4 = new CustomTreeNode(4L, 2L, "子4", "1");
        CustomTreeNode node5 = new CustomTreeNode(5L, 2L, "子5", "1");
        CustomTreeNode node6 = new CustomTreeNode(6L, 3L, "子6", "1");
        CustomTreeNode node7 = new CustomTreeNode(7L, 3L, "子7", "1");
        nodeList.add(node1);
        nodeList.add(node2);
        nodeList.add(node3);
        nodeList.add(node4);
        nodeList.add(node5);
        nodeList.add(node6);
        nodeList.add(node7);
        System.out.println(nodeList.stream().map(node -> {
            RootTreeNode n = new RootTreeNode();
            n.setId(node.getId());
            n.setParentId(node.getParentId());
            n.setLabel(node.getLabel());
            return n;
        }).collect(Collectors.toList()));
//        System.out.println(getTreeList(nodeList, 0L));

//        TreeNodeInterface<MenuNode> nodeInterface = MenuNode::new;
//        RootTreeNode treeNode = nodeInterface.createTreeNode(1L, 2L, "1", "2");
//        System.out.println(((MenuNode) treeNode).getType());
    }


}
