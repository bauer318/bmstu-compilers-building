package ru.bmstu.kibamba.grammars;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static ru.bmstu.kibamba.grammars.ProductionUtils.*;

public class UnreachableCharacterEliminator {

    public static Grammar eliminatesUnreachableCharacter(Grammar grammarWithOnlyNecessaryNonterminals) {
        Grammar clonedGrammar = grammarWithOnlyNecessaryNonterminals.clone();
        Set<String> reachableCharacters = performStep01(grammarWithOnlyNecessaryNonterminals);
        Set<String> nonterminals = new LinkedHashSet<>(reachableCharacters);
        nonterminals.retainAll(clonedGrammar.getNonterminals());
        Set<String> terminals = new LinkedHashSet<>(reachableCharacters);
        terminals.retainAll(clonedGrammar.getTerminals());
        List<Production> productions = new ArrayList<>();

        for (Production production : clonedGrammar.getProductions()) {
            var chains = getProductionChainsArray(production);
            for (String chain : chains) {
                var canAdd = true;
                var tokens = getProductionTokenArray(chain);
                for (String token : tokens) {
                    if (!isAlphaBelongSet(token, reachableCharacters)) {
                        canAdd = false;
                        break;
                    }
                }
                if (canAdd) {
                    productions.add(new Production(production.getNonterminal(), chain));
                }
            }
        }
        return new Grammar(nonterminals, terminals, productions, clonedGrammar.getFirstSymbol());
    }

    private static Set<String> performStep01(Grammar grammarWithOnlyNecessaryNonterminals) {
        Grammar clonedGrammar = grammarWithOnlyNecessaryNonterminals.clone();
        Set<String> initialFirstSymbols = new LinkedHashSet<>();
        initialFirstSymbols.add(clonedGrammar.getFirstSymbol());
        return performStep02(clonedGrammar, initialFirstSymbols);
    }

    private static Set<String> performStep02(Grammar clonedGrammar, Set<String> prevFirstSymbols) {
        Set<String> currentFirstSymbols = new LinkedHashSet<>();
        for (Production production : clonedGrammar.getProductions()) {
            if (isAlphaBelongSet(production.getNonterminal(), prevFirstSymbols)) {
                var chains = getProductionChainsArray(production);
                for (String chain : chains) {
                    var tokens = getProductionTokenArray(chain);
                    currentFirstSymbols.addAll(tokens);
                }
            }
        }
        currentFirstSymbols.addAll(prevFirstSymbols);
        return performStep03(clonedGrammar, currentFirstSymbols, prevFirstSymbols);
    }

    private static Set<String> performStep03(Grammar clonedGrammar, Set<String> currentFirstSymbols, Set<String> prevFirstSymbols) {
        if (!currentFirstSymbols.equals(prevFirstSymbols)) {
            return performStep02(clonedGrammar, currentFirstSymbols);
        } else {
            return performStep04(currentFirstSymbols);
        }

    }

    private static Set<String> performStep04(Set<String> currentFirstSymbols) {
        return currentFirstSymbols;
    }
}
