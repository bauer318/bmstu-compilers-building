package ru.bmstu.kibamba.parsing;

import ru.bmstu.kibamba.models.*;

import java.util.*;

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
        return new GrammarSymbol("$");
    }

    public static GrammarSymbol buildEpsilon() {
        return new GrammarSymbol("£");
    }

}
