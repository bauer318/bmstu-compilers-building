package ru.bmstu.kibamba.parsing;

import ru.bmstu.kibamba.dto.TerminalFunctionResponse;
import ru.bmstu.kibamba.models.*;

import java.util.*;

import static ru.bmstu.kibamba.parsing.TerminalBuilder.*;

public class ParserUtils {
    public static boolean isEpsilon(GrammarSymbol symbol) {
        return symbol.getName().equals("£");
    }

    public static boolean isTerminal(GrammarSymbol symbol, Set<Terminal> terminals) {
        for (Terminal terminal : terminals) {
            if (terminal.getName().equals(symbol.getName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNonterminal(GrammarSymbol symbol,
                                        Set<Nonterminal> nonterminals) {
        for (Nonterminal nonterminal : nonterminals) {
            if (nonterminal.getName().equals(symbol.getName())) {
                return true;
            }
        }
        return false;
    }

    private static StringBuilder addNodeAdditionalInfos(TreeNode node) {
        if (node.children.isEmpty()) {
            var vValue = node.value.getValue() == null ?
                    node.value.getName() : node.value.getValue();
            return new StringBuilder(vValue + " ");
        }
        List<TreeNode> children = node.children;
        StringBuilder sbResult = new StringBuilder();
        for (TreeNode child : children) {
            sbResult.append(addNodeAdditionalInfos(child));
        }
        return sbResult;
    }

    public static TerminalFunctionResponse buildTerminalFunctionResponse(TreeNode treeNode) {
        treeNode.value.setValue(treeNode.value.getName());
        treeNode.setAdditionalInfo(addNodeAdditionalInfos(treeNode));
        return new TerminalFunctionResponse(treeNode, true);
    }

    public static TerminalFunctionResponse buildTerminalFunctionResponse() {
        return new TerminalFunctionResponse(false);
    }

    public static TreeNode buildNonterminalNode(String nonTerminalName) {
        return new TreeNode(new Nonterminal(nonTerminalName, "val"));
    }

    public static TreeNode buildEpsilonNode() {
        return new TreeNode(new GrammarSymbol("£", ""));
    }

    public static TreeNode buildTerminalNode(GrammarSymbol terminal) {
        var node = new TreeNode(terminal);
        if (noNeedAdditionalInfos(terminal)) {
            node.resetAdditionalInfos();
        } else {
            node.setAdditionalInfo(addNodeAdditionalInfos(node));
        }
        return node;
    }

    private static boolean noNeedAdditionalInfos(GrammarSymbol terminal) {
        return isBegin(terminal) || isEnd(terminal) ||
                terminal.equals(buildTerminalSemicolon()) ||
                terminal.equals(buildTerminalIs()) || isOperator(terminal);
    }

    private static boolean isOperator(GrammarSymbol terminal) {
        return terminal.equals(buildTerminalAdd()) ||
                terminal.equals(buildTerminalSub()) ||
                terminal.equals(buildTerminalMul()) ||
                terminal.equals(buildTerminalDiv()) ||
                terminal.equals(buildTerminalLess()) ||
                terminal.equals(buildTerminalLessEqual()) ||
                terminal.equals(buildTerminalEqual()) ||
                terminal.equals(buildTerminalGreat()) ||
                terminal.equals(buildTerminalGreatEqual()) ||
                terminal.equals(buildTerminalNotEqual());
    }

    public static Set<Production> getNonterminalProductions(GrammarSymbol nonterminal,
                                                            Set<Production> productions) {
        Set<Production> result = new LinkedHashSet<>();
        for (Production production : productions) {
            if (production.getNonterminal().equals(nonterminal)) {
                result.add(production);
            }
        }
        return result;
    }

    public static Production buildProduction(Nonterminal nonterminal, GrammarSymbol... chainSymbols) {
        Set<GrammarSymbol> chains = new LinkedHashSet<>();
        Collections.addAll(chains, chainSymbols);
        ProductionChain productionChain = new ProductionChain(chains);
        return new Production(nonterminal, productionChain);
    }

    public static GrammarSymbol buildInputEndMarkerSymbol() {
        return new GrammarSymbol("$", "");
    }

    public static GrammarSymbol buildEpsilon() {
        return new GrammarSymbol("£", "");
    }

}
