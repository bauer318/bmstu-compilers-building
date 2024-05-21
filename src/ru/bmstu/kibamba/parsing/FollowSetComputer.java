package ru.bmstu.kibamba.parsing;

import ru.bmstu.kibamba.models.Grammar;
import ru.bmstu.kibamba.models.GrammarSymbol;
import ru.bmstu.kibamba.models.Nonterminal;
import ru.bmstu.kibamba.models.Production;

import java.util.*;

import static ru.bmstu.kibamba.parsing.ParserUtils.*;

public class FollowSetComputer {
    private final Grammar grammar;
    private final Map<GrammarSymbol, Set<GrammarSymbol>> firstSets;
    private final Map<GrammarSymbol, Set<GrammarSymbol>> followSets;

    public FollowSetComputer(Grammar grammar,
                             Map<GrammarSymbol, Set<GrammarSymbol>> firstSets) {
        this.grammar = grammar;
        this.firstSets = firstSets;
        this.followSets = new LinkedHashMap<>();
        initFollowSets(grammar.getNonterminals());

    }

    public void computeFollowSets() {
        boolean changed;
        var epsilon = buildEpsilon();
        addInputEndMarker(grammar.getStart());
        do {
            changed = false;

            for (Nonterminal nonterminal : grammar.getNonterminals()) {
                for (Production production : getNonterminalProductions(nonterminal, grammar.getProductions())) {
                    var symbols = new ArrayList<>(production.getChain().getChain());
                    for (var i = 0; i < symbols.size(); i++) {
                        var symbol = symbols.get(i);

                        if (isNonterminal(symbol, grammar.getNonterminals())) {
                            Set<GrammarSymbol> followSet = followSets.get(symbol);
                            int originalSize = followSet.size();

                            //Look ahead in the production, case A -> aBb
                            if (i + 1 < symbols.size()) {
                                GrammarSymbol nextSymbol = symbols.get(i + 1);
                                if (isTerminal(nextSymbol, grammar.getTerminals())) {
                                    followSet.add(nextSymbol);
                                } else {
                                    Set<GrammarSymbol> firstSetOfNext = firstSets.get(nextSymbol);
                                    if (firstSetOfNext != null) {
                                        followSet.addAll(firstSetOfNext);
                                        followSet.remove(epsilon);
                                    }

                                    //if £ ε FIRST(nextSymbol) , then add FOLLOW(nonTerminal)
                                    if (firstSetOfNext.contains(epsilon)) {
                                        followSet.addAll(followSets.get(nonterminal));
                                    }
                                }
                            } else {
                                //If B is at the end of the production, case A -> aB
                                followSet.addAll(followSets.get(nonterminal));
                            }

                            if (followSet.size() > originalSize) {
                                changed = true;
                            }
                        }
                    }
                }
            }
        } while (changed);
    }

    public Map<GrammarSymbol, Set<GrammarSymbol>> getFollowSets() {
        return followSets;
    }

    private void initFollowSets(Set<Nonterminal> nonterminals) {
        for (Nonterminal nonterminal : nonterminals) {
            followSets.put(nonterminal, new LinkedHashSet<>());
        }
    }

    private void addInputEndMarker(GrammarSymbol start) {
        followSets.get(start).add(buildInputEndMarkerSymbol());
    }

}
