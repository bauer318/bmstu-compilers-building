package ru.bmstu.kibamba.grammars;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static ru.bmstu.kibamba.grammars.ProductionUtils.getProductionChainsArray;
import static ru.bmstu.kibamba.grammars.ProductionUtils.getProductionTokenArray;

public class LanguageNonEmptinessChecker {

    public static Set<String> performStep01(Grammar grammar) {
        Set<String> emptyNonterminals = new HashSet<>();
        Grammar clonedGrammar = grammar.clone();

        return performStep02(clonedGrammar, emptyNonterminals);
    }

    private static Set<String> performStep02(Grammar grammar, Set<String> predNonterminals) {
        Set<String> currentNonterminals = new LinkedHashSet<>();
        for (Production production : grammar.getProductions()) {
            var chains = getProductionChainsArray(production);
            for (String chain : chains) {
                if (isAlphaBelongSet(chain, predNonterminals) || isAlphaBelongSet(chain, grammar.getTerminals())
                        || chain.equals("£")) {
                    currentNonterminals.add(production.getNonterminal());
                    break;
                }
            }
        }
        currentNonterminals.addAll(predNonterminals);
        return performStep03(grammar, currentNonterminals, predNonterminals);
    }

    private static Set<String> performStep03(Grammar grammar, Set<String> currentNonterminals, Set<String> predNonterminals) {
        if (!currentNonterminals.equals(predNonterminals)) {
            return performStep02(grammar, currentNonterminals);
        } else {
            //performStep04(grammar, currentNonterminals);
            return currentNonterminals;
        }
    }

    private static void performStep04(Grammar grammar, Set<String> eNonterminals) {
        if (eNonterminals.contains(grammar.getFirstSymbol())) {
            System.out.println("ДА");
        } else {
            System.out.println("НЕТ");
        }
    }

    private static boolean isAlphaBelongSet(String alpha, Set<String> terminalsNonterminals) {
        var alphaTokens = getProductionTokenArray(alpha);
        for (String token : alphaTokens) {
            if (!terminalsNonterminals.contains(token)) {
                return false;
            }
        }
        return true;
    }


}
