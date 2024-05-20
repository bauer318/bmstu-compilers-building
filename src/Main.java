import ru.bmstu.kibamba.models.*;
import ru.bmstu.kibamba.parsing.ParserUtils;

import java.util.LinkedHashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Set<Nonterminal> nonterminals = new LinkedHashSet<>();
        var nonTerminalA = new Nonterminal("A");
        var nonTerminalB = new Nonterminal("B");
        nonterminals.add(nonTerminalA);
        nonterminals.add(nonTerminalB);

        Set<Terminal> terminals = new LinkedHashSet<>();
        var terminal = new Terminal("a","a");
        terminals.add(terminal);

        var epsilon = new GrammarSymbol("£");
        Set<Production> productions = new LinkedHashSet<>();
        Set<GrammarSymbol> chain = new LinkedHashSet<>();
        chain.add(nonTerminalB);
        //chain.add(nonTerminalA);
        productions.add(new Production(nonTerminalA,new ProductionChain(chain)));
        chain = new LinkedHashSet<>();
        //chain.add(nonTerminal);
        chain.add(epsilon);
        productions.add(new Production(nonTerminalB,new ProductionChain(chain)));

        System.out.println(ParserUtils.isGeneratesZeroOrMoreEpsilon(nonTerminalA,productions,nonterminals));


    }
}