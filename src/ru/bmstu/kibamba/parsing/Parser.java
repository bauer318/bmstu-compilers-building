package ru.bmstu.kibamba.parsing;

import ru.bmstu.kibamba.dto.TerminalFunctionResponse;
import ru.bmstu.kibamba.models.Grammar;
import ru.bmstu.kibamba.models.GrammarSymbol;
import ru.bmstu.kibamba.models.Terminal;

import java.util.List;

import static ru.bmstu.kibamba.parsing.ParserUtils.*;

public class Parser {
    private final Grammar grammar;
    private final List<GrammarSymbol> input;
    private int currentIndex;
    private TreeNode root;

    private int erFlag;

    public Parser(Grammar grammar, List<GrammarSymbol> input) {
        this.grammar = grammar;
        this.input = input;
    }

    public GrammarSymbol getCurrentInputSymbol() {
        return input.get(currentIndex);
    }

    private TerminalFunctionResponse S() {
        TreeNode sNode = new TreeNode(grammar.getStart());
        var a = getCurrentInputSymbol();
        if (a.equals(new Terminal("begin", "begin"))) {
            currentIndex++;
            sNode.addChild(buildTerminalNode(a));
            var l = L();
            if (l.isResult()) {
                sNode.addChild(l.getNode());
                a = getCurrentInputSymbol();
                if (a.equals(new Terminal("end", "end"))) {
                    sNode.addChild(buildTerminalNode(a));
                    return buildTerminalFunctionResponse(sNode);
                } else {
                    erFlag++;
                    printError(1, "end", a.getName());
                    return buildTerminalFunctionResponse();
                }
            } else {
                printError(2, "L", a.getName());
            }
        }
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse L() {
        TreeNode lNode = buildNonterminalNode("L");
        var a = getCurrentInputSymbol();
        var o = O();
        if (o.isResult()) {
            lNode.addChild(o.getNode());
            var b = B();
            if (b.isResult()) {
                lNode.addChild(b.getNode());
                return buildTerminalFunctionResponse(lNode);
            } else {
                printError(3, " B after O ", a.getName());
                return buildTerminalFunctionResponse();
            }
        }
        printError(4, "O", a.getName());
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse B() {
        TreeNode bNode = buildNonterminalNode("B");
        var lPrime = LPrime();
        if (lPrime.isResult()) {
            bNode.addChild(lPrime.getNode());
            return buildTerminalFunctionResponse(bNode);
        }
        bNode.addChild(buildEpsilonNode());
        return buildTerminalFunctionResponse(bNode);
    }

    private TerminalFunctionResponse O() {
        TreeNode oNode = buildNonterminalNode("O");
        var a = getCurrentInputSymbol();
        if (a.equals(new Terminal("var", "var"))) {
            oNode.addChild(buildTerminalNode(a));
            currentIndex++;
            a = getCurrentInputSymbol();
            if (a.equals(new Terminal(":=", "is"))) {
                oNode.addChild(buildTerminalNode(a));
                currentIndex++;
                var x = X();
                if (x.isResult()) {
                    oNode.addChild(x.getNode());
                    return buildTerminalFunctionResponse(oNode);
                } else {
                    printError(6, "X", a.getName());
                    return buildTerminalFunctionResponse();
                }
            } else {
                printError(7, ":=", a.getName());
                return buildTerminalFunctionResponse();
            }
        }
        printError(8, "var", a.getName());
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse LPrime() {
        TreeNode lPrimeNode = buildNonterminalNode("L'");
        var a = getCurrentInputSymbol();
        if (a.equals(new Terminal(";", "semicolon"))) {
            lPrimeNode.addChild(buildTerminalNode(a));
            currentIndex++;
            var o = O();
            if (o.isResult()) {
                lPrimeNode.addChild(o.getNode());
                var b = B();
                if (b.isResult()) {
                    lPrimeNode.addChild(b.getNode());
                    return buildTerminalFunctionResponse(lPrimeNode);
                } else {
                    printError(9, "B", a.getName());
                    return buildTerminalFunctionResponse();
                }
            } else {
                printError(10, "O", a.getName());
                return buildTerminalFunctionResponse();
            }
        }
        printError(11, ";", a.getName());
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse EPrime() {
        TreeNode ePrimeNode = buildNonterminalNode("E'");
        var a = A();
        if (a.isResult()) {
            ePrimeNode.addChild(a.getNode());
            var t = T();
            if (t.isResult()) {
                ePrimeNode.addChild(t.getNode());
                var c = C();
                if (c.isResult()) {
                    ePrimeNode.addChild(c.getNode());
                    return buildTerminalFunctionResponse(ePrimeNode);
                }
                printError(23, "C", "Others ");
                return buildTerminalFunctionResponse();
            }
            printError(24, "T", "Others ");
            return buildTerminalFunctionResponse();
        }
        printError(25, "A", "Others ");
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse TPrime() {
        TreeNode tPrimeNode = buildNonterminalNode("T'");
        var m = M();
        if (m.isResult()) {
            tPrimeNode.addChild(m.getNode());
            var f = F();
            if (f.isResult()) {
                tPrimeNode.addChild(f.getNode());
                var d = D();
                if (d.isResult()) {
                    tPrimeNode.addChild(d.getNode());
                    return buildTerminalFunctionResponse(tPrimeNode);
                }
                printError(27, "D", "Others ");
                return buildTerminalFunctionResponse();
            }
            printError(28, "F", "Others ");
            return buildTerminalFunctionResponse();
        }
        printError(29, "M", "Others ");
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse X() {
        TreeNode xNode = buildNonterminalNode("X");
        var a = getCurrentInputSymbol();
        var e = E();
        if (e.isResult()) {
            xNode.addChild(e.getNode());
            var xPrime = XPrime();
            if (xPrime.isResult()) {
                xNode.addChild(xPrime.getNode());
                return buildTerminalFunctionResponse(xNode);
            }
            printError(12, "X'", a.getName());

        }
        printError(13, "E", a.getName());
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse XPrime() {
        TreeNode xPrimeNode = buildNonterminalNode("X'");
        var r = R();
        if (r.isResult()) {
            xPrimeNode.addChild(r.getNode());
            var e = E();
            if (e.isResult()) {
                xPrimeNode.addChild(e.getNode());
                return buildTerminalFunctionResponse(xPrimeNode);
            }
            printError(14, "E", "Others ");
            return buildTerminalFunctionResponse();
        }
        xPrimeNode.addChild(buildEpsilonNode());
        return buildTerminalFunctionResponse(xPrimeNode);
    }

    private TerminalFunctionResponse E() {
        TreeNode eNode = buildNonterminalNode("E");
        var t = T();
        if (t.isResult()) {
            eNode.addChild(t.getNode());
            var c = C();
            if (c.isResult()) {
                eNode.addChild(c.getNode());
                return buildTerminalFunctionResponse(eNode);
            }
            printError(15, "C", "Others ");
            return buildTerminalFunctionResponse();
        }
        printError(16, "T", "Others ");
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse C() {
        TreeNode cNode = buildNonterminalNode("C");
        var ePrime = EPrime();
        if (ePrime.isResult()) {
            cNode.addChild(ePrime.getNode());
            return buildTerminalFunctionResponse(cNode);
        }
        cNode.addChild(buildEpsilonNode());
        return buildTerminalFunctionResponse(cNode);
    }

    private TerminalFunctionResponse T() {
        TreeNode tNode = buildNonterminalNode("T");
        var f = F();
        if (f.isResult()) {
            tNode.addChild(f.getNode());
            var d = D();
            if (d.isResult()) {
                tNode.addChild(d.getNode());
                return buildTerminalFunctionResponse(tNode);
            }
            printError(17, "D", "Others ");
            return buildTerminalFunctionResponse();
        }
        printError(18, "F", "Others ");
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse D() {
        TreeNode dNode = buildNonterminalNode("D");
        var tPrime = TPrime();
        if (tPrime.isResult()) {
            dNode.addChild(tPrime.getNode());
            return buildTerminalFunctionResponse(dNode);
        }
        dNode.addChild(buildEpsilonNode());
        return buildTerminalFunctionResponse(dNode);
    }

    private TerminalFunctionResponse F() {
        TreeNode fNode = buildNonterminalNode("F");
        var a = getCurrentInputSymbol();
        if (a.equals(new Terminal("var", "var"))) {
            currentIndex++;
            fNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(fNode);
        }

        if (a.equals(new Terminal("const", "const"))) {
            currentIndex++;
            fNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(fNode);
        }

        if (a.equals(new Terminal("(", "lParen"))) {
            fNode.addChild(buildTerminalNode(a));
            currentIndex++;
            var e = E();
            if (e.isResult()) {
                fNode.addChild(e.getNode());
                a = getCurrentInputSymbol();
                if (a.equals(new Terminal(")", "rParen"))) {
                    currentIndex++;
                    fNode.addChild(buildTerminalNode(a));
                    return buildTerminalFunctionResponse(fNode);
                }
                printError(19, ")", a.getName());
                return buildTerminalFunctionResponse();
            }
            printError(20, "E", "Others ");
            return buildTerminalFunctionResponse();
        }

        printError(21, "var , const or (E)", "Others ");
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse M() {
        var a = getCurrentInputSymbol();
        TreeNode mNode = buildNonterminalNode("M");
        if (a.equals(new Terminal("*", "MUL"))) {
            currentIndex++;
            mNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(mNode);
        } else if (a.equals(new Terminal("/", "DIV"))) {
            currentIndex++;
            mNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(mNode);
        }
        printError(1, "* or /", a.getName());
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse A() {
        var a = getCurrentInputSymbol();
        TreeNode aNode = buildNonterminalNode("A");
        if (a.equals(new Terminal("+", "ADD"))) {
            currentIndex++;
            aNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(aNode);
        } else if (a.equals(new Terminal("-", "SUB"))) {
            currentIndex++;
            aNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(aNode);
        }
        printError(2, "+ or -", a.getName());
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse R() {
        var a = getCurrentInputSymbol();
        TreeNode rNode = buildNonterminalNode("R");

        if (a.equals(new Terminal("<", "L"))) {
            currentIndex++;
            rNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(rNode);
        } else if (a.equals(new Terminal("<=", "LE"))) {
            currentIndex++;
            rNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(rNode);
        } else if (a.equals(new Terminal("=", "E"))) {
            currentIndex++;
            rNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(rNode);
        } else if (a.equals(new Terminal("<>", "NE"))) {
            currentIndex++;
            rNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(rNode);
        } else if (a.equals(new Terminal(">", "G"))) {
            currentIndex++;
            rNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(rNode);
        } else if (a.equals(new Terminal(">=", "GE"))) {
            currentIndex++;
            rNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(rNode);
        }
        printError(22, "<, >, <=, >=, =,<>", a.getName());
        return buildTerminalFunctionResponse();
    }

    private void printError(int errorNumber, String expected, String found) {
        System.out.println("ERROR " + errorNumber + " expected " + expected + " but found " + found);
    }

    public boolean parseS() {
        currentIndex = 0;
        erFlag = 0;
        TerminalFunctionResponse response = S();
        root = response.getNode();
        if (response.isResult()) {
            return true;
        }
        return false;
    }

    public TreeNode getRoot() {
        return root;
    }
}
