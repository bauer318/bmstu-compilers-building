import ru.bmstu.kibamba.models.*;
import ru.bmstu.kibamba.parsing.Parser;
import ru.bmstu.kibamba.parsing.ParserUtils;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Set<Nonterminal> nonterminals = new LinkedHashSet<>();
        Set<Production> productions = new LinkedHashSet<>();
        Set<Terminal> terminals = new LinkedHashSet<>();

        var nonTerminalS = new Nonterminal("S", true);
        var nonTerminalL = new Nonterminal("L");
        var nonTerminalB = new Nonterminal("B");
        var nonTerminalO = new Nonterminal("O");
        var nonTerminalX = new Nonterminal("X");
        var nonTerminalXPrime = new Nonterminal("X'");
        var nonTerminalE = new Nonterminal("E");
        var nonTerminalC = new Nonterminal("C");
        var nonTerminalT = new Nonterminal("T");
        var nonTerminalD = new Nonterminal("D");
        var nonTerminalF = new Nonterminal("F");
        var nonTerminalR = new Nonterminal("R");
        var nonTerminalA = new Nonterminal("A");
        var nonTerminalM = new Nonterminal("M");
        var nonTerminalLPrime = new Nonterminal("L'");
        var nonTerminalEPrime = new Nonterminal("E'");
        var nonTerminalTPrime = new Nonterminal("T'");


        Collections.addAll(nonterminals, nonTerminalS, nonTerminalL, nonTerminalB,
                nonTerminalO, nonTerminalX, nonTerminalXPrime, nonTerminalE, nonTerminalC,
                nonTerminalT, nonTerminalD, nonTerminalF, nonTerminalR, nonTerminalA, nonTerminalM,
                nonTerminalLPrime, nonTerminalEPrime, nonTerminalTPrime);

        var terminalBegin = new Terminal("begin", "begin");
        var terminalEnd = new Terminal("end", "end");
        var terminalVar = new Terminal("var", "var");
        var terminalIs = new Terminal(":=", "is");
        var terminalSemicolon = new Terminal(";", "semicolon");
        var terminalConst = new Terminal("const", "const");
        var terminalLParen = new Terminal("(", "lParen");
        var terminalRParen = new Terminal(")", "rParen");
        var terminalMul = new Terminal("*", "MUL");
        var terminalDiv = new Terminal("/", "DIV");
        var terminalAdd = new Terminal("+", "ADD");
        var terminalSub = new Terminal("-", "SUB");
        var terminalLess = new Terminal("<", "L");
        var terminalLessEqual = new Terminal("<=", "LE");
        var terminalEqual = new Terminal("=", "E");
        var terminalNotEqual = new Terminal("<>", "NE");
        var terminalGreat = new Terminal(">", "G");
        var terminalGreatEqual = new Terminal(">=", "GE");

        var epsilon = new GrammarSymbol("£");

        Collections.addAll(terminals, terminalBegin, terminalEnd,
                terminalVar, terminalIs, terminalSemicolon, terminalConst,
                terminalLParen, terminalRParen, terminalMul, terminalDiv, terminalAdd,
                terminalLess, terminalEqual, terminalLessEqual, terminalNotEqual,
                terminalGreat, terminalGreatEqual, terminalSub);


        var productionS = ParserUtils.buildProduction(nonTerminalS, terminalBegin, nonTerminalL, terminalEnd);

        var productionL = ParserUtils.buildProduction(nonTerminalL, nonTerminalO, nonTerminalB);
        var productionB = ParserUtils.buildProduction(nonTerminalB, nonTerminalLPrime);
        var productionBSc = ParserUtils.buildProduction(nonTerminalB, epsilon);
        var productionO = ParserUtils.buildProduction(nonTerminalO, terminalVar, terminalIs, nonTerminalX);
        var productionX = ParserUtils.buildProduction(nonTerminalX, nonTerminalE, nonTerminalXPrime);
        var productionXPrime = ParserUtils.buildProduction(nonTerminalXPrime, nonTerminalR, nonTerminalE);
        var productionXPrimeSc = ParserUtils.buildProduction(nonTerminalXPrime, epsilon);
        var productionE = ParserUtils.buildProduction(nonTerminalE, nonTerminalT, nonTerminalC);
        var productionC = ParserUtils.buildProduction(nonTerminalC, nonTerminalEPrime);
        var productionCSc = ParserUtils.buildProduction(nonTerminalC, epsilon);
        var productionT = ParserUtils.buildProduction(nonTerminalT, nonTerminalF, nonTerminalD);
        var productionD = ParserUtils.buildProduction(nonTerminalD, nonTerminalTPrime);
        var productionDSc = ParserUtils.buildProduction(nonTerminalD, epsilon);
        var productionF = ParserUtils.buildProduction(nonTerminalF, terminalVar);
        var productionFSc = ParserUtils.buildProduction(nonTerminalF, terminalConst);
        var productionFTh = ParserUtils.buildProduction(nonTerminalF, terminalLParen, nonTerminalE, terminalRParen);
        var productionRL = ParserUtils.buildProduction(nonTerminalR, terminalLess);
        var productionRLE = ParserUtils.buildProduction(nonTerminalR, terminalLessEqual);
        var productionRE = ParserUtils.buildProduction(nonTerminalR, terminalEqual);
        var productionLPrime = ParserUtils.buildProduction(nonTerminalLPrime, terminalSemicolon, nonTerminalO, nonTerminalB);
        var productionEPrime = ParserUtils.buildProduction(nonTerminalEPrime, nonTerminalA, nonTerminalT, nonTerminalC);
        var productionTPrime = ParserUtils.buildProduction(nonTerminalTPrime, nonTerminalM, nonTerminalF, nonTerminalD);
        var productionRNE = ParserUtils.buildProduction(nonTerminalR, terminalNotEqual);
        var productionRG = ParserUtils.buildProduction(nonTerminalR, terminalGreat);
        var productionRGE = ParserUtils.buildProduction(nonTerminalR, terminalGreatEqual);
        var productionAAdd = ParserUtils.buildProduction(nonTerminalA, terminalAdd);
        var productionASub = ParserUtils.buildProduction(nonTerminalA, terminalSub);
        var productionMMul = ParserUtils.buildProduction(nonTerminalM, terminalMul);
        var productionMDiv = ParserUtils.buildProduction(nonTerminalM, terminalDiv);

        Collections.addAll(productions, productionS, productionL, productionB, productionBSc, productionO,
                productionX, productionXPrime, productionXPrimeSc, productionE, productionC, productionCSc,
                productionT, productionD, productionDSc, productionF, productionFSc, productionFTh,
                productionRL, productionRLE, productionRE, productionRNE, productionRG, productionRGE, productionAAdd,
                productionASub, productionMMul, productionMDiv, productionLPrime, productionEPrime, productionTPrime);


        Grammar grammar = new Grammar(nonterminals, terminals, nonTerminalS, productions);

        List<GrammarSymbol> input = new ArrayList<>();
        Collections.addAll(input, terminalBegin, terminalVar, terminalIs, terminalConst,
                terminalLess, terminalVar, terminalEnd);
        Parser parser = new Parser(grammar, input);

        var t = parser.parseS();
        System.out.println(parser.getRoot());

    }

    private static void oldTest() {
        Set<Nonterminal> nonterminals = new LinkedHashSet<>();
        Set<Terminal> terminals = new LinkedHashSet<>();
        Set<Production> productions = new LinkedHashSet<>();

        /*var nonTerminalE = new Nonterminal("E", true);
        var nonTerminalEPrime = new Nonterminal("E'");
        var nonTerminalT = new Nonterminal("T");
        var nonTerminalTPrime = new Nonterminal("T'");
        var nonTerminalF = new Nonterminal("F");*/
        var nonTerminalS = new Nonterminal("S", true);
        var nonTerminalA = new Nonterminal("A");
        //var nonTerminalB = new Nonterminal("B");
        /*Collections.addAll(nonterminals, nonTerminalE, nonTerminalEPrime, nonTerminalT,
                nonTerminalTPrime,nonTerminalF);*/
        Collections.addAll(nonterminals, nonTerminalS, nonTerminalA);

        /*var terminalId = new Terminal("id", "ID");
        var terminalPlus = new Terminal("+", "ADD");
        var terminalMul = new Terminal("*", "MUL");
        var terminalLeftParam = new Terminal("(", "LEFT_PARAM");
        var terminalRightParam = new Terminal(")", "RIGHT_PARAM");*/

        var terminalA = new Terminal("a", "a");
        var terminalB = new Terminal("b", "b");
        var terminalC = new Terminal("c", "c");
        var terminalD = new Terminal("d", "d");
        /*Collections.addAll(terminals, terminalId, terminalPlus,terminalMul,
                terminalLeftParam,terminalRightParam);*/

        Collections.addAll(terminals, terminalA, terminalB, terminalC, terminalD);

        var epsilon = new GrammarSymbol("£");

        /*var productionE = ParserUtils.buildProduction(nonTerminalE, nonTerminalT, nonTerminalEPrime);

        var productionEPrime = ParserUtils.buildProduction(nonTerminalEPrime, terminalPlus, nonTerminalT,nonTerminalEPrime);
        var productionEPrimeSc = ParserUtils.buildProduction(nonTerminalEPrime, epsilon);

        var productionT = ParserUtils.buildProduction(nonTerminalT, nonTerminalF, nonTerminalTPrime);

        var productionTPrime = ParserUtils.buildProduction(nonTerminalTPrime, terminalMul, nonTerminalF, nonTerminalTPrime);
        var productionTPrimeSc = ParserUtils.buildProduction(nonTerminalTPrime, epsilon);

        var productionF = ParserUtils.buildProduction(nonTerminalF, terminalLeftParam, nonTerminalE, terminalRightParam);
        var productionFSc = ParserUtils.buildProduction(nonTerminalF, terminalId);*/

        var productionS = ParserUtils.buildProduction(nonTerminalS,
                terminalC, nonTerminalA, terminalD);
        /*var productionSSc = ParserUtils.buildProduction(nonTerminalS,
                terminalB);*/

        var productionA = ParserUtils.buildProduction(nonTerminalA, terminalA, terminalB);
        var productionASc = ParserUtils.buildProduction(nonTerminalA, terminalA);

        /*var productionB = ParserUtils.buildProduction(nonTerminalB,
                nonTerminalS);*/

        /*Collections.addAll(productions, productionE, productionEPrime, productionEPrimeSc,
                productionT,productionTPrime,productionTPrimeSc,productionF,productionFSc);*/
        Collections.addAll(productions, productionS, productionA, productionASc);


        Grammar grammar = new Grammar(nonterminals, terminals, nonTerminalS, productions);

        List<GrammarSymbol> input = new ArrayList<>();
        Collections.addAll(input, terminalC, terminalA, terminalD);
        Parser parser = new Parser(grammar, input);
        /*var a  = parser.next();
        var b = parser.next();
        var c = parser.next();*/

        var t = parser.parseS();
        System.out.println(parser.getRoot());

        /*FirstSetComputer computer = new FirstSetComputer(grammar);

        computer.computerFirstSets();

        var a = computer.getFirstSets();

        FollowSetComputer followSetComputer = new FollowSetComputer(grammar,a);
        followSetComputer.computeFollowSets();

        var b = followSetComputer.getFollowSets();*/

        //var firsts = ParserUtils.computesFirst(nonterminals, terminals, productions);

    }
}