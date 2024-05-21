package ru.bmstu.kibamba.parsing;

import ru.bmstu.kibamba.models.*;

import java.util.*;

public class ParserUtils {
    public static Set<GrammarSymbol> FIRST(GrammarSymbol x,
                                           Set<Nonterminal> nonterminals,
                                           Set<Terminal> terminals,
                                           Set<Production> productions) {
        Set<GrammarSymbol> firstX = new LinkedHashSet<>();
        if (isEpsilon(x)) {
            firstX.add(x);
        } else if (isTerminal(x, terminals)) {
            firstX.add(x);
        } else {
            GrammarSymbol epsilon = new GrammarSymbol("£");
            if (isGeneratesEpsilon(x, productions)) {
                firstX.add(epsilon);
            }
            for (Production production : productions) {
                if (production.getNonterminal().equals(x)) {
                    var alpha = production.getChain().getChain();
                    var addEpsilonSet = true;
                    for (GrammarSymbol symbol : alpha) {
                        if (!isGeneratesZeroOrMoreEpsilon(symbol,
                                productions, nonterminals)) {
                            var currentYFirst = FIRST(symbol, nonterminals, terminals, productions);
                            firstX.addAll(currentYFirst);
                            break;
                        } else {
                            var currentYFirst = FIRST(symbol, nonterminals, terminals, productions);
                            Set<GrammarSymbol> epsilonSet = new LinkedHashSet<>();
                            epsilonSet.add(epsilon);
                            currentYFirst.removeAll(epsilonSet);
                            firstX.addAll(currentYFirst);
                            addEpsilonSet = false;
                            break;
                        }
                    }

                    if (isGeneratesEpsilon(production, nonterminals, productions) && addEpsilonSet) {
                        Set<GrammarSymbol> epsilonSet = new LinkedHashSet<>();
                        epsilonSet.add(epsilon);
                        firstX.addAll(epsilonSet);
                    }
                }
            }
        }

        return firstX;
    }

    public static Set<GrammarSymbol> computesFirst(GrammarSymbol X,
                                                   Set<Nonterminal> nonterminals,
                                                   Set<Terminal> terminals,
                                                   Set<Production> productions) {
        Set<GrammarSymbol> firstX = new LinkedHashSet<>();
        if (isTerminal(X, terminals)) {
            firstX.add(X);
            return firstX;
        }

        if (isNonterminal(X, nonterminals)) {
            var xProductions = getNonterminalProductions(X, productions);
            for (Production production : xProductions) {
                var alpha = new ArrayList<>(production.getChain().getChain());
                for (var i = 0; i < alpha.size(); i++) {
                    var aSet = computesFirst(alpha.get(i), nonterminals,
                            terminals, productions);
                    if (aSet.size() >= 1) {
                        var a = aSet.stream().findFirst().get();
                        if (isTerminal(a, terminals)) {
                            var canAddA = true;
                            for (var k = 0; k < i; k++) {
                                if (!isGeneratesZeroOrMoreEpsilon(alpha.get(k), productions, nonterminals)) {
                                    canAddA = false;
                                    break;
                                }
                            }
                            if (canAddA) {
                                firstX.add(a);
                                break;
                            }
                        }
                    }

                    if (isGeneratesEpsilon(production, nonterminals, productions)) {
                        Set<GrammarSymbol> epsilonSet = new LinkedHashSet<>();
                        epsilonSet.add(new GrammarSymbol("£"));
                        firstX.addAll(epsilonSet);
                    }

                    if (isGeneratesZeroOrMoreEpsilon(alpha.get(i), productions, nonterminals)) {
                        if (i < alpha.size()) {
                            i++;
                            firstX.addAll(computesFirst(alpha.get(i), nonterminals,
                                    terminals, productions));
                        }
                    }

                }

            }
        }

        if (isGeneratesEpsilon(X, productions)) {
            Set<GrammarSymbol> epsilonSet = new LinkedHashSet<>();
            epsilonSet.add(new GrammarSymbol("£"));
            firstX.addAll(epsilonSet);
            return firstX;
        }


        return firstX;
    }

    public static Map<GrammarSymbol, Set<GrammarSymbol>> computesFirst(Set<Nonterminal> nonterminals,
                                                                       Set<Terminal> terminals,
                                                                       Set<Production> productions) {
        Set<GrammarSymbol> epsilonSet = new LinkedHashSet<>();
        epsilonSet.add(new GrammarSymbol("£"));

        Map<GrammarSymbol, Set<GrammarSymbol>> result = new HashMap<>();
        for (GrammarSymbol X : nonterminals) {
            var firstX = computesFirst(X, nonterminals, terminals, productions);
            if (result.containsKey(X)) {
                result.get(X).addAll(firstX);
            } else {
                result.put(X, firstX);
            }
        }
        return result;
    }

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

    public static boolean isGeneratesEpsilon(GrammarSymbol x, Set<Production> productions) {
        for (Production production : productions) {
            if (production.getNonterminal().equals(x)) {
                if (isGeneratesEpsilon(production)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isGeneratesZeroOrMoreEpsilon(GrammarSymbol x,
                                                       Set<Production> grammarProductions,
                                                       Set<Nonterminal> nonterminals) {
        var result = false;
        for (Production production : grammarProductions) {
            if (production.getNonterminal().equals(x)) {
                if (isGeneratesEpsilon(production)) {
                    return true;
                } else {
                    var chain = production.getChain().getChain();
                    for (GrammarSymbol symbol : chain) {
                        if (isNonterminal(symbol, nonterminals)) {
                            var currentProductions =
                                    getNonterminalProductions(symbol, grammarProductions);
                            if (isGeneratesEpsilon(symbol, currentProductions)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    private static boolean isGeneratesEpsilon(Production currentProduction) {
        GrammarSymbol epsilon = new GrammarSymbol("£");
        var chain = currentProduction.getChain();
        return chain.getChain().size() == 1 && chain.getChain().stream().findFirst()
                .get().equals(epsilon);
    }

    public static boolean isGeneratesEpsilon(Production currentProduction,
                                             Set<Nonterminal> nonterminals,
                                             Set<Production> grammarProductions) {
        var result = true;
        var chain = currentProduction.getChain().getChain();
        for (GrammarSymbol symbol : chain) {
            if (isNonterminal(symbol, nonterminals)) {
                if (!isGeneratesZeroOrMoreEpsilon(symbol, grammarProductions, nonterminals)) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return result;
    }

    private static boolean isNonterminal(GrammarSymbol symbol,
                                         Set<Nonterminal> nonterminals) {
        for (Nonterminal nonterminal : nonterminals) {
            if (nonterminal.getName().equals(symbol.getName())) {
                return true;
            }
        }
        return false;
    }

    private static Set<Production> getNonterminalProductions(GrammarSymbol nonterminal,
                                                             Set<Production> productions) {
        Set<Production> result = new HashSet<>();
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
}
