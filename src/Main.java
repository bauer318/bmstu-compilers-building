import ru.bmstu.kibamba.models.*;
import ru.bmstu.kibamba.parsing.FirstSetComputer;
import ru.bmstu.kibamba.parsing.FollowSetComputer;
import ru.bmstu.kibamba.parsing.ParserUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Set<Nonterminal> nonterminals = new LinkedHashSet<>();
        Set<Terminal> terminals = new LinkedHashSet<>();
        Set<Production> productions = new LinkedHashSet<>();

        var nonTerminalE = new Nonterminal("E", true);
        var nonTerminalEPrime = new Nonterminal("E'");
        var nonTerminalT = new Nonterminal("T");
        var nonTerminalTPrime = new Nonterminal("T'");
        var nonTerminalF = new Nonterminal("F");
        /*var nonTerminalS = new Nonterminal("S", true);
        var nonTerminalA = new Nonterminal("A");
        var nonTerminalB = new Nonterminal("B");*/
        Collections.addAll(nonterminals, nonTerminalE, nonTerminalEPrime, nonTerminalT,
                nonTerminalTPrime,nonTerminalF);

        var terminalId = new Terminal("id", "ID");
        var terminalPlus = new Terminal("+", "ADD");
        var terminalMul = new Terminal("*", "MUL");
        var terminalLeftParam = new Terminal("(", "LEFT_PARAM");
        var terminalRightParam = new Terminal(")", "RIGHT_PARAM");

        /*var terminalA = new Terminal("a", "a");
        var terminalB = new Terminal("b", "b");*/
        Collections.addAll(terminals, terminalId, terminalPlus,terminalMul,
                terminalLeftParam,terminalRightParam);

        var epsilon = new GrammarSymbol("£");

        var productionE = ParserUtils.buildProduction(nonTerminalE, nonTerminalT, nonTerminalEPrime);

        var productionEPrime = ParserUtils.buildProduction(nonTerminalEPrime, terminalPlus, nonTerminalT,nonTerminalEPrime);
        var productionEPrimeSc = ParserUtils.buildProduction(nonTerminalEPrime, epsilon);

        var productionT = ParserUtils.buildProduction(nonTerminalT, nonTerminalF, nonTerminalTPrime);

        var productionTPrime = ParserUtils.buildProduction(nonTerminalTPrime, terminalMul, nonTerminalF, nonTerminalTPrime);
        var productionTPrimeSc = ParserUtils.buildProduction(nonTerminalTPrime, epsilon);

        var productionF = ParserUtils.buildProduction(nonTerminalF, terminalLeftParam, nonTerminalE, terminalRightParam);
        var productionFSc = ParserUtils.buildProduction(nonTerminalF, terminalId);

        /*var productionS = ParserUtils.buildProduction(nonTerminalS,
                nonTerminalA, nonTerminalB);
        var productionSSc = ParserUtils.buildProduction(nonTerminalS,
                terminalB);

        var productionA = ParserUtils.buildProduction(nonTerminalA, epsilon);
        var productionASc = ParserUtils.buildProduction(nonTerminalA, terminalA);

        var productionB = ParserUtils.buildProduction(nonTerminalB,
                nonTerminalS);*/

        Collections.addAll(productions, productionE, productionEPrime, productionEPrimeSc,
                productionT,productionTPrime,productionTPrimeSc,productionF,productionFSc);

        Grammar grammar = new Grammar(nonterminals, terminals,nonTerminalE, productions);

        FirstSetComputer computer = new FirstSetComputer(grammar);

        computer.computerFirstSets();

        var a = computer.getFirstSets();

        FollowSetComputer followSetComputer = new FollowSetComputer(grammar,a);
        followSetComputer.computeFollowSets();

        var b = followSetComputer.getFollowSets();

       //var firsts = ParserUtils.computesFirst(nonterminals, terminals, productions);

    }
}