package ru.bmstu.kibamba.grammars;

import java.util.*;

import static ru.bmstu.kibamba.grammars.GrammarUtils.*;

public class LanguageNonEmptinessChecker {

    /**
     * eliminates unnecessary non-terminals
     *
     * @param grammar input grammar to eliminate unnecessary non-terminals
     * @return Grammar with only non-terminals that can generate terminals chain
     */
    public static Grammar eliminatesUnnecessaryNonterminals(Grammar grammar) {
        Grammar clonedGrammar = grammar.clone();
        Set<String> nENonterminals = performStep01(grammar);

        clonedGrammar.getNonterminals().retainAll(nENonterminals);
        List<Production> productions = new ArrayList<>();

        for (Production production : grammar.getProductions()) {
            var chains = getProductionChainsArray(production);
            for (String chain : chains) {
                if (isAlphaBelongSet(chain, clonedGrammar.getTerminals()) ||
                        isAlphaBelongSet(chain, grammar.getTerminals())) {
                    productions.add(new Production(production.getNonterminal(), chain));

                }
            }
        }

        return new Grammar(clonedGrammar.getNonterminals(),
                grammar.getTerminals(),
                productions,
                grammar.getFirstSymbol());
    }

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



}
