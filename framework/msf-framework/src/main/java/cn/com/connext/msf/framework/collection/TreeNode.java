package cn.com.connext.msf.framework.collection;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;

public interface TreeNode {

    String ROOT_ID = "0";

    String getId();

    String getParent();

    String getName();

    String getRemark();

    default <S extends TreeNode> List<String> getChild(List<S> nodeList) {
        return getChild(this, nodeList, Lists.newArrayList());
    }

    static <S extends TreeNode> List<String> getChild(TreeNode treeNode, List<S> nodeList, List<String> childList) {
        for (TreeNode node : nodeList) {
            if (Objects.equals(treeNode.getId(), node.getParent())) {
                childList.add(node.getId());
                getChild(node, nodeList, childList);
            }
        }
        return childList;
    }

}
