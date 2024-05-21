package ru.bmstu.kibamba.parsing;

import ru.bmstu.kibamba.models.*;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static ru.bmstu.kibamba.parsing.ParserUtils.*;

public class FirstSetComputer {
    private final Grammar grammar;
    private final Map<GrammarSymbol, Set<GrammarSymbol>> firstSets;

    public FirstSetComputer(Grammar grammar) {
        this.grammar = grammar;
        this.firstSets = new LinkedHashMap<>();

        initFirstSets(grammar.getNonterminals());

        initGrammarSymbolFirstSets(grammar.getProductions());
    }

    private void initFirstSets(Set<Nonterminal> nonterminals) {
        for (Nonterminal nonterminal : nonterminals) {
            firstSets.put(nonterminal, new LinkedHashSet<>());
        }
    }

    private void initGrammarSymbolFirstSets(Set<Production> productions) {
        for (Production production : productions) {
            var symbols = production.getChain().getChain();
            for (GrammarSymbol symbol : symbols) {
                if (!firstSets.containsKey(symbol) &&
                        !isTerminal(symbol, grammar.getTerminals()) &&
                        !isEpsilon(symbol)) {
                    firstSets.put(symbol, new LinkedHashSet<>());
                }
            }
        }
    }
    public void computerFirstSets() {
        boolean changed;
        do {
            changed = false;
            GrammarSymbol epsilon = new GrammarSymbol("£");
            for (Nonterminal nonterminal : grammar.getNonterminals()) {
                Set<GrammarSymbol> firstSet = firstSets.get(nonterminal);
                int originalSize = firstSet.size();

                for (Production production : getNonterminalProductions(nonterminal, grammar.getProductions())) {
                    var symbols = production.getChain().getChain();
                    for (GrammarSymbol symbol : symbols) {
                        if (isTerminal(symbol, grammar.getTerminals())
                                || isEpsilon(symbol)) {
                            firstSet.add(symbol);
                            break;
                        } else {
                            Set<GrammarSymbol> firstSetOfSymbol = firstSets.get(symbol);
                            if (firstSetOfSymbol != null) {
                                firstSet.addAll(firstSetOfSymbol);
                                firstSet.remove(epsilon);
                            }

                            if (firstSetOfSymbol == null ||
                                    !firstSetOfSymbol.contains(epsilon)) {
                                break;
                            }
                        }
                    }

                    boolean allNullable = true;
                    for (GrammarSymbol symbol : symbols) {
                        Set<GrammarSymbol> firstSetOfSymbol = firstSets.get(symbol);
                        if (firstSetOfSymbol == null || !firstSetOfSymbol.contains(epsilon)) {
                            allNullable = false;
                            break;
                        }
                    }
                    if (allNullable) {
                        firstSet.add(epsilon);
                    }
                }
                if (firstSet.size() > originalSize) {
                    changed = true;
                }
            }

        } while (changed);
    }

    public Map<GrammarSymbol, Set<GrammarSymbol>> getFirstSets() {
        return firstSets;
    }
}
