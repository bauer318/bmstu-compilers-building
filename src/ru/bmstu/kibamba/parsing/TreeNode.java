package ru.bmstu.kibamba.parsing;

import java.util.ArrayList;
import java.util.List;

class TreeNode {
    String value;
    List<TreeNode> children;

    TreeNode(String value) {
        this.value = value;
        this.children = new ArrayList<>();
    }

    void addChild(TreeNode child) {
        children.add(child);
    }

    @Override
    public String toString() {
        return toString("", true);
    }

    private String toString(String indent, boolean isRoot) {
        StringBuilder sb = new StringBuilder();
        sb.append(isRoot ? "" : indent).append(isRoot ? "" : "|---").append(value).append("\n");
        indent = isRoot ? "" : "    ";
        for (TreeNode child : children) {
            sb.append(child.toString(indent, false));
        }
        return sb.toString();
    }
}
