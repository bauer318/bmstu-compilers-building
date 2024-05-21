package ru.bmstu.kibamba.parsing;

import ru.bmstu.kibamba.models.*;

import java.util.ArrayList;
import java.util.List;

import static ru.bmstu.kibamba.parsing.ParserUtils.getNonterminalProductions;
import static ru.bmstu.kibamba.parsing.ParserUtils.isNonterminal;

public class Parser {
    private final Grammar grammar;
    private final List<GrammarSymbol> input;

    private int currentIndex;

    private TreeNode root;

    public Parser(Grammar grammar, List<GrammarSymbol> input) {
        this.grammar = grammar;
        this.input = input;
        //currentIndex = 0;
    }

    public GrammarSymbol next() {
        return input.get(currentIndex);
    }

    public void parse() {
        //var a = next();
        //var startProductions = getNonterminalProductions(grammar.getStart(), grammar.getProductions());
        var start = grammar.getStart();
        parse(start);
    }

    private void parse(Production production) {
        var inputIndex = currentIndex;
        var a = next();
        var symbols = new ArrayList<>(production.getChain().getChain());
        for (var i = 0; i < symbols.size(); i++) {
            var Xi = symbols.get(i);
            if (isNonterminal(Xi, grammar.getNonterminals())) {
                //call Xi procedure
                if (Xi.equals(new Nonterminal("S"))) {
                    S();
                } else {
                    A();
                }
            } else if (Xi.equals(a)) {
                //To next input symbol
                System.out.println(production.getNonterminal() + " -> " + Xi);
                a = next();
            } else {
                System.out.println("Error");
                currentIndex = inputIndex;
                break;
            }
        }
    }

    private void parse(GrammarSymbol Xi) {
        var currentProductions = getNonterminalProductions(Xi, grammar.getProductions());
        for (Production production : currentProductions) {
            parse(production);
        }
    }

    private boolean A() {
        var a = next();
        TreeNode A_Node = new TreeNode("A");
        root.addChild(A_Node);
        if (a.equals(new Terminal("a", "a"))) {
            currentIndex++;
            A_Node.addChild(new TreeNode("a"));
            a = next();
            if (a.equals(new Terminal("b", "b"))) {
                A_Node.addChild(new TreeNode("b"));
                return true;
            } else {
                currentIndex--;
            }
            return true;
        }
        return false;
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
                    System.out.println("ERROR expected d but found " + a.getName());
                    return false;
                }
            } else {
                System.out.println("ERROR expected A but found " + a.getName());
            }
        }
        return false;
    }

    public boolean parseS() {
        currentIndex = 0;
        if (S()) {
            return true;
        }
        return false;
    }

    public TreeNode getRoot() {
        return root;
    }
}
