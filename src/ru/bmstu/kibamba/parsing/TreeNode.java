package ru.bmstu.kibamba.parsing;

import ru.bmstu.kibamba.models.GrammarSymbol;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    GrammarSymbol value;
    List<TreeNode> children;

    StringBuilder additionalInfo;

    TreeNode(GrammarSymbol value) {
        this.value = value;
        this.children = new ArrayList<>();
        additionalInfo = new StringBuilder(" = ");
    }

    public void resetAdditionalInfos(){
        this.additionalInfo = new StringBuilder("");
    }

    public StringBuilder getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(StringBuilder additionalInfo) {
        this.additionalInfo.append(additionalInfo);
    }

    void addChild(TreeNode child) {
        children.add(child);
    }

    @Override
    public String toString() {
        return toString("", true, true);
    }

    private String toString(String indent, boolean isLast, boolean isRoot) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbValue = new StringBuilder(value.getName());
        sbValue.append(value.getAttribute().isEmpty() ? "" : "." + value.getAttribute());
        sb.append(isRoot ? "" : indent).append(isRoot ? "" : "|--").append(sbValue).append(additionalInfo).append("\n");
        for (int i = 0; i < children.size(); i++) {
            var child = children.get(i);
            sb.append(child.toString(indent + (isLast ? isRoot ? "" : "   " : "|  "),
                    i == children.size() - 1, false));
        }
        return sb.toString();
    }
}
