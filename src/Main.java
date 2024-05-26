import ru.bmstu.kibamba.files.TerminalFileReader;
import ru.bmstu.kibamba.models.*;
import ru.bmstu.kibamba.parsing.Parser;
import ru.bmstu.kibamba.parsing.ParserUtils;

import java.util.*;

import static ru.bmstu.kibamba.parsing.TerminalBuilder.*;

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

        var terminalBegin = buildTerminalBegin();
        var terminalEnd = buildTerminalEnd();
        var terminalVar = buildTerminalId();
        var terminalIs = buildTerminalIs();
        var terminalSemicolon = buildTerminalSemicolon();
        var terminalConst = buildTerminalConst();
        var terminalLParen = buildTerminalLParen();
        var terminalRParen = buildTerminalRParen();
        var terminalMul = buildTerminalMul();
        var terminalDiv = buildTerminalDiv();
        var terminalAdd = buildTerminalAdd();
        var terminalSub = buildTerminalSub();
        var terminalLess = buildTerminalLess();
        var terminalLessEqual = buildTerminalLessEqual();
        var terminalEqual = buildTerminalEqual();
        var terminalNotEqual = buildTerminalNotEqual();
        var terminalGreat = buildTerminalGreat();
        var terminalGreatEqual = buildTerminalGreatEqual();

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

        List<GrammarSymbol> inputExample01 = TerminalFileReader.buildInputChain("example01");

        List<GrammarSymbol> inputExample02 = TerminalFileReader.buildInputChain("example02");

        List<GrammarSymbol> inputWithError = TerminalFileReader.buildInputChain("example_with_error");

        Parser parser = new Parser(grammar, inputWithError);
        var isParsed = parser.parse();
        if (isParsed) {
            System.out.println(parser.getRoot());
        }


    }
}