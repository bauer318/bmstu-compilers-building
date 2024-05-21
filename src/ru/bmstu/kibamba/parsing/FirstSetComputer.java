package ru.bmstu.kibamba.parsing;

import ru.bmstu.kibamba.models.*;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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
                        !isTerminal(symbol) && !isEpsilon(symbol)) {
                    firstSets.put(symbol, new LinkedHashSet<>());
                }
            }
        }
    }

    private boolean isTerminal(GrammarSymbol grammarSymbol) {
        for (Terminal terminal : grammar.getTerminals()) {
            if (terminal.getName().equals(grammarSymbol.getName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isEpsilon(GrammarSymbol grammarSymbol) {
        return grammarSymbol.getName().equals("£");
    }

    public void computerFirstSets() {
        boolean changed;
        do {
            changed = false;
            GrammarSymbol epsilon = new GrammarSymbol("£");
            for (Nonterminal nonterminal : grammar.getNonterminals()) {
                Set<GrammarSymbol> firstSet = firstSets.get(nonterminal);
                int originalSize = firstSet.size();

                for (Production production : getNonterminalProductions(nonterminal)) {
                    var symbols = production.getChain().getChain();
                    for (GrammarSymbol symbol : symbols) {
                        if (isTerminal(symbol) || isEpsilon(symbol)) {
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

    private Set<Production> getNonterminalProductions(Nonterminal nonterminal) {
        Set<Production> productions = new LinkedHashSet<>();

        for (Production production : grammar.getProductions()) {
            if (production.getNonterminal().equals(nonterminal)) {
                productions.add(production);
            }
        }

        return productions;
    }

    public Map<GrammarSymbol, Set<GrammarSymbol>> getFirstSets() {
        return firstSets;
    }
}
