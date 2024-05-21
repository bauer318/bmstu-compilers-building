package ru.bmstu.kibamba.parsing;

import ru.bmstu.kibamba.models.Grammar;
import ru.bmstu.kibamba.models.GrammarSymbol;
import ru.bmstu.kibamba.models.Terminal;

import java.util.List;

public class Parser {
    private final Grammar grammar;
    private final List<GrammarSymbol> input;
    private int currentIndex;
    private TreeNode root;

    private int erFlag;

    public Parser(Grammar grammar, List<GrammarSymbol> input) {
        this.grammar = grammar;
        this.input = input;
    }

    public GrammarSymbol next() {
        return input.get(currentIndex);
    }

    private boolean S() {
        root = new TreeNode(grammar.getStart().getName());
        var a = next();
        if (a.equals(new Terminal("c", "c"))) {
            currentIndex++;
            root.addChild(new TreeNode("c"));
            if (A()) {
                currentIndex++;
                a = next();
                if (a.equals(new Terminal("d", "d"))) {
                    root.addChild(new TreeNode("d"));
                    return true;
                } else {
                    erFlag++;
                    System.out.println("ERROR 1 expected d but found " + a.getName());
                    return false;
                }
            } else {
                System.out.println("ERROR expected A but found " + a.getName());
            }
        }
        return false;
    }

    private boolean M() {
        var a = next();
        TreeNode mNode = new TreeNode("M");
        root.addChild(mNode);
        if (a.equals(new Terminal("*", "MUL"))) {
            currentIndex++;
            mNode.addChild(new TreeNode("*"));
            return true;
        } else if (a.equals(new Terminal("/", "DIV"))) {
            currentIndex++;
            mNode.addChild(new TreeNode("/"));
            return true;
        }
        printError(1, "* or /", a.getName());
        return false;
    }

    private boolean A() {
        var a = next();
        TreeNode aNode = new TreeNode("A");
        root.addChild(aNode);
        if (a.equals(new Terminal("+", "ADD"))) {
            currentIndex++;
            aNode.addChild(new TreeNode("+"));
            return true;
        } else if (a.equals(new Terminal("-", "SUB"))) {
            currentIndex++;
            aNode.addChild(new TreeNode("-"));
            return true;
        }
        printError(2, "+ or -", a.getName());
        return false;
    }

    private boolean R() {
        var a = next();
        TreeNode rNode = new TreeNode("R");
        root.addChild(rNode);
        if (a.equals(new Terminal("<", "L"))) {
            currentIndex++;
            rNode.addChild(new TreeNode("<"));
            return true;
        } else if (a.equals(new Terminal("<=", "LE"))) {
            currentIndex++;
            rNode.addChild(new TreeNode("<="));
            return true;
        } else if (a.equals(new Terminal("=", "E"))) {
            currentIndex++;
            rNode.addChild(new TreeNode("="));
            return true;
        }
        return false;
    }

    private void printError(int errorNumber, String expected, String found) {
        System.out.println("ERROR " + errorNumber + " expected " + expected + " but found " + found);
    }

    public boolean parseS() {
        currentIndex = 0;
        erFlag = 0;
        if (S()) {
            return true;
        }
        return false;
    }

    public TreeNode getRoot() {
        return root;
    }
}
