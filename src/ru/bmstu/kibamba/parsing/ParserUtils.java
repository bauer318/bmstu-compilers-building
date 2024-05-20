package ru.bmstu.kibamba.parsing;

import ru.bmstu.kibamba.models.GrammarSymbol;
import ru.bmstu.kibamba.models.Nonterminal;
import ru.bmstu.kibamba.models.Production;
import ru.bmstu.kibamba.models.Terminal;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

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
                if(production.getNonterminal().equals(x)){
                    var alpha = production.getChain().getChain();
                    for (GrammarSymbol symbol : alpha) {
                        if (isNonterminal(symbol, nonterminals)) {
                            if (!isGeneratesZeroOrMoreEpsilon(symbol,
                                    productions,nonterminals)) {
                                firstX.addAll(FIRST(symbol,nonterminals,terminals,productions));
                                break;
                            }else{
                                var currentYFirst = FIRST(symbol,nonterminals,terminals,productions);
                                Set<GrammarSymbol> epsilonSet = new LinkedHashSet<>();
                                epsilonSet.add(epsilon);
                                currentYFirst.removeAll(epsilonSet);
                                firstX.addAll(currentYFirst);
                            }
                        }
                    }

                }

            }
        }

        return firstX;
    }

    /*private static Set<Terminal> FIRST(GrammarSymbol x, Set<Production> productions){
        Set<Terminal> result =
    }*/

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
                                                        Set<Production> productions,
                                                        Set<Nonterminal> nonterminals) {
        var result = false;
        for (Production production : productions) {
            if (production.getNonterminal().equals(x)) {
                if (isGeneratesEpsilon(production)) {
                    return true;
                } else {
                    var chain = production.getChain().getChain();
                    for (GrammarSymbol symbol : chain) {
                        if (isNonterminal(symbol, nonterminals)) {
                            var currentProductions =
                                    getNonterminalProductions(symbol, productions);
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

    private static boolean isGeneratesEpsilon(Production production) {
        GrammarSymbol epsilon = new GrammarSymbol("£");
        var chain = production.getChain();
        return chain.getChain().size() == 1 && chain.getChain().stream().findFirst()
                .get().equals(epsilon);
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
}
