package cn.com.connext.msf.framework.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreeItem implements TreeNode {

    private String id;

    @Indexed
    private String parent;

    private String name;

    private List<TreeItem> child;

    private Object extend;

    public TreeItem() {

    }

    public static List<TreeItem> convert(List<TreeNode> nodeList) {
        List<TreeItem> itemList = Lists.newArrayList();
        for (TreeNode node : nodeList) {
            if (Objects.equals(ROOT_ID, node.getParent())) {
                itemList.add(findChild(node, nodeList));
            }
        }
        return itemList;
    }

    private static TreeItem findChild(TreeNode parentNode, List<TreeNode> nodeList) {
        TreeItem parentItem = from(parentNode);
        for (TreeNode childNode : nodeList) {
            if (Objects.equals(parentItem.id, childNode.getParent())) {
                if (parentItem.child == null) {
                    parentItem.child = Lists.newArrayList();
                }
                parentItem.child.add(findChild(childNode, nodeList));
            }
        }
        return parentItem;
    }

    private static TreeItem from(TreeNode node) {
        TreeItem treeItem = new TreeItem();
        treeItem.id = node.getId();
        treeItem.parent = node.getParent();
        treeItem.name = node.getName();
        treeItem.extend = node.getExtend();
        return treeItem;
    }


    public String getId() {
        return id;
    }

    public String getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public List<TreeItem> getChild() {
        return child;
    }

    public Object getExtend() {
        return extend;
    }
}
