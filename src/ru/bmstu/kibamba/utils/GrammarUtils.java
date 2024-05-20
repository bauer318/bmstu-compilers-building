package ru.bmstu.kibamba.utils;

import ru.bmstu.kibamba.models.Grammar;
import ru.bmstu.kibamba.models.Nonterminal;
import ru.bmstu.kibamba.models.Production;
import ru.bmstu.kibamba.models.Terminal;

import java.util.Set;

public class GrammarUtils {
    public static Grammar buildGrammar(Set<Nonterminal> nonterminals,
                                       Set<Terminal> terminals,
                                       Set<Production> productions) {
        Nonterminal startSymbol = getStartSymbol(nonterminals);
        if(startSymbol==null){
            startSymbol = nonterminals.stream().findFirst().get();
        }
        return new Grammar(nonterminals, terminals, startSymbol, productions);
    }

    private static Nonterminal getStartSymbol(Set<Nonterminal> nonterminals) {
        return nonterminals.stream().filter(Nonterminal::isStartSymbol).findFirst().orElse(null);
    }
}
