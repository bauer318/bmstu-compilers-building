package ru.bmstu.kibamba.dto;

import ru.bmstu.kibamba.parsing.TreeNode;

public class TerminalFunctionResponse {
    private TreeNode node;
    private boolean result;

    public TerminalFunctionResponse(TreeNode node, boolean result) {
        this.node = node;
        this.result = result;
    }

    public TerminalFunctionResponse(boolean result) {
        this.result = result;
    }

    public TreeNode getNode() {
        return node;
    }

    public void setNode(TreeNode node) {
        this.node = node;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
