package ru.bmstu.kibamba.parsing;

import ru.bmstu.kibamba.dto.TerminalFunctionResponse;
import ru.bmstu.kibamba.models.Grammar;
import ru.bmstu.kibamba.models.GrammarSymbol;

import java.util.ArrayList;
import java.util.List;

import static ru.bmstu.kibamba.parsing.ParserUtils.*;
import static ru.bmstu.kibamba.parsing.TerminalBuilder.*;

public class Parser {
    private final Grammar grammar;
    private final List<GrammarSymbol> input;
    private int currentIndex;
    private TreeNode root;

    private int erFlag;

    private List<String> errorsTrace = new ArrayList<>();

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
        if (a.equals(buildTerminalBegin())) {
            currentIndex++;
            sNode.addChild(buildTerminalNode(a));
            var l = L();
            if (l.isResult()) {
                sNode.addChild(l.getNode());
                a = getCurrentInputSymbol();
                if (a.equals(buildTerminalEnd())) {
                    sNode.addChild(buildTerminalNode(a));
                    return buildTerminalFunctionResponse(sNode);
                } else {
                    erFlag++;
                    addErrorTrace(erFlag, "end", a.getName());
                    incrementFlag();
                    return buildTerminalFunctionResponse();
                }
            } else {
                addErrorTrace(erFlag, "L", a.getName());
            }
        }
        incrementFlag();
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
                incrementFlag();
                addErrorTrace(erFlag, " B after O ", a.getName());
                return buildTerminalFunctionResponse();
            }
        }
        incrementFlag();
        addErrorTrace(erFlag, "O", a.getName());
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
        if (a.equals(buildTerminalId())) {
            oNode.addChild(buildTerminalNode(a));
            currentIndex++;
            a = getCurrentInputSymbol();
            if (a.equals(buildTerminalIs())) {
                oNode.addChild(buildTerminalNode(a));
                currentIndex++;
                var x = X();
                if (x.isResult()) {
                    oNode.addChild(x.getNode());
                    return buildTerminalFunctionResponse(oNode);
                } else {
                    incrementFlag();
                    addErrorTrace(erFlag, "X", a.getName());
                    return buildTerminalFunctionResponse();
                }
            } else {
                incrementFlag();
                addErrorTrace(erFlag, ":=", a.getName());
                return buildTerminalFunctionResponse();
            }
        }
        incrementFlag();
        addErrorTrace(erFlag, "id", a.getName());
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse LPrime() {
        TreeNode lPrimeNode = buildNonterminalNode("L'");
        var a = getCurrentInputSymbol();
        if (a.equals(buildTerminalSemicolon())) {
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
                    incrementFlag();
                    addErrorTrace(erFlag, "B", a.getName());
                    return buildTerminalFunctionResponse();
                }
            } else {
                incrementFlag();
                addErrorTrace(erFlag, "O", a.getName());
                return buildTerminalFunctionResponse();
            }
        }
        incrementFlag();
        addErrorTrace(erFlag, ";", a.getName());
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse EPrime() {
        TreeNode ePrimeNode = buildNonterminalNode("E'");
        var a = A();
        if (a.isResult()) {
            ePrimeNode.addChild(a.getNode());
            return TC(ePrimeNode);
        }
        incrementFlag();
        addErrorTrace(erFlag, "A", "Others ");
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse TC(TreeNode node) {
        var t = T();
        if (t.isResult()) {
            node.addChild(t.getNode());
            var c = C();
            if (c.isResult()) {
                node.addChild(c.getNode());
                return buildTerminalFunctionResponse(node);
            }
            incrementFlag();
            addErrorTrace(erFlag, "C", "Others ");
            return buildTerminalFunctionResponse();
        }
        incrementFlag();
        addErrorTrace(erFlag, "T", "Others ");
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse TPrime() {
        TreeNode tPrimeNode = buildNonterminalNode("T'");
        var m = M();
        if (m.isResult()) {
            tPrimeNode.addChild(m.getNode());
            return FD(tPrimeNode);
        }
        incrementFlag();
        addErrorTrace(erFlag, "M", "Others ");
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
            incrementFlag();
            addErrorTrace(erFlag, "X'", a.getName());

        }
        incrementFlag();
        addErrorTrace(erFlag, "E", a.getName());
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
            incrementFlag();
            addErrorTrace(erFlag, "E", "Others ");
            return buildTerminalFunctionResponse();
        }
        xPrimeNode.addChild(buildEpsilonNode());
        return buildTerminalFunctionResponse(xPrimeNode);
    }

    private TerminalFunctionResponse E() {
        TreeNode eNode = buildNonterminalNode("E");
        return TC(eNode);
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
        return FD(tNode);
    }

    private TerminalFunctionResponse FD(TreeNode node) {
        var f = F();
        if (f.isResult()) {
            node.addChild(f.getNode());
            var d = D();
            if (d.isResult()) {
                node.addChild(d.getNode());
                return buildTerminalFunctionResponse(node);
            }
            incrementFlag();
            addErrorTrace(erFlag, "D", "Others ");
            return buildTerminalFunctionResponse();
        }
        incrementFlag();
        addErrorTrace(erFlag, "F", "Others ");
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
        if (a.equals(buildTerminalId())) {
            currentIndex++;
            fNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(fNode);
        }

        if (a.equals(buildTerminalConst())) {
            currentIndex++;
            fNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(fNode);
        }

        if (a.equals(buildTerminalLParen())) {
            fNode.addChild(buildTerminalNode(a));
            currentIndex++;
            var e = E();
            if (e.isResult()) {
                fNode.addChild(e.getNode());
                a = getCurrentInputSymbol();
                if (a.equals(buildTerminalRParen())) {
                    currentIndex++;
                    fNode.addChild(buildTerminalNode(a));
                    return buildTerminalFunctionResponse(fNode);
                }
                incrementFlag();
                addErrorTrace(erFlag, ")", a.getName());
                return buildTerminalFunctionResponse();
            }
            incrementFlag();
            addErrorTrace(erFlag, "E", "Others ");
            return buildTerminalFunctionResponse();
        }
        incrementFlag();
        addErrorTrace(erFlag, "id , const or (E)", "Others ");
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse M() {
        var a = getCurrentInputSymbol();
        TreeNode mNode = buildNonterminalNode("M");
        if (a.equals(buildTerminalMul())) {
            currentIndex++;
            mNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(mNode);
        } else if (a.equals(buildTerminalDiv())) {
            currentIndex++;
            mNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(mNode);
        }
        incrementFlag();
        addErrorTrace(erFlag, "* or /", a.getName());
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse A() {
        var a = getCurrentInputSymbol();
        TreeNode aNode = buildNonterminalNode("A");
        if (a.equals(buildTerminalAdd())) {
            currentIndex++;
            aNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(aNode);
        } else if (a.equals(buildTerminalSub())) {
            currentIndex++;
            aNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(aNode);
        }
        incrementFlag();
        addErrorTrace(erFlag, "+ or -", a.getName());
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse R() {
        var a = getCurrentInputSymbol();
        TreeNode rNode = buildNonterminalNode("R");

        if (a.equals(buildTerminalLess())) {
            currentIndex++;
            rNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(rNode);
        } else if (a.equals(buildTerminalLessEqual())) {
            currentIndex++;
            rNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(rNode);
        } else if (a.equals(buildTerminalEqual())) {
            currentIndex++;
            rNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(rNode);
        } else if (a.equals(buildTerminalNotEqual())) {
            currentIndex++;
            rNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(rNode);
        } else if (a.equals(buildTerminalGreat())) {
            currentIndex++;
            rNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(rNode);
        } else if (a.equals(buildTerminalGreatEqual())) {
            currentIndex++;
            rNode.addChild(buildTerminalNode(a));
            return buildTerminalFunctionResponse(rNode);
        }
        incrementFlag();
        addErrorTrace(erFlag, "<, >, <=, >=, =,<>", a.getName());
        return buildTerminalFunctionResponse();
    }

    private void addErrorTrace(int errorNumber, String expected, String found) {
        errorsTrace.add("ERROR " + errorNumber + " expected " + expected + " but found " + found + "\n");
    }

    private void printErrorTrace() {
        for (String error : errorsTrace) {
            System.out.print(error);
        }
    }

    private void incrementFlag() {
        erFlag++;
    }

    public boolean parse() {
        currentIndex = 0;
        erFlag = 0;
        TerminalFunctionResponse response = S();
        root = response.getNode();
        if (response.isResult()) {
            return true;
        } else {
            if (erFlag > 0) {
                System.out.println("INTERNAL ERROR");
                System.out.println("Not expected \'"+getCurrentInputSymbol().getName()+"\' at "+currentIndex+" position ");
            } else {
                System.out.println("Position " + currentIndex);
                System.out.println("Error: Incorrect  first symbol of S!");
            }
            /*System.out.println("\nTrace");*/
            //printErrorTrace();
        }
        return false;
    }

    public TreeNode getRoot() {
        return root;
    }
}
