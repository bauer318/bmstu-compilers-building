package ru.bmstu.kibamba.parsing;

import ru.bmstu.kibamba.models.GrammarSymbol;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    GrammarSymbol value;
    List<TreeNode> children;

    TreeNode(GrammarSymbol value) {
        this.value = value;
        this.children = new ArrayList<>();
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
        sb.append(isRoot ? "" : indent).append(isRoot ? "" : "|--").append(value.getName()).append("\n");
        for (int i = 0; i < children.size(); i++) {
            sb.append(children.get(i).toString(indent + (isLast ? isRoot ? "" : "   " : "|  "),
                    i == children.size() - 1, false));
        }
        return sb.toString();
    }
}
