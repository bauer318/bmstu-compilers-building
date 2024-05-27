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

    /**
     * S -> begin L end
     *
     * @return postfix annotation of S -> L begin end
     */
    private TerminalFunctionResponse SPostfix() {
        TreeNode sNode = new TreeNode(grammar.getStart());
        var lPostfix = LPostfix();
        if (lPostfix.isResult()) {
            sNode.addChild(lPostfix.getNode());
            var a = getCurrentInputSymbol();
            if (a.equals(buildTerminalBegin())) {
                currentIndex++;
                sNode.addChild(buildTerminalNode(a));
                a = getCurrentInputSymbol();
                if (a.equals(buildTerminalEnd())) {
                    sNode.addChild(buildTerminalNode(a));
                    return buildTerminalFunctionResponse(sNode);
                } else {
                    incrementFlag();
                    addErrorTrace(erFlag, "end", a.getName());
                    return buildTerminalFunctionResponse();
                }
            }
            incrementFlag();
            addErrorTrace(erFlag, "begin", a.getName());
            return buildTerminalFunctionResponse();
        }
        incrementFlag();
        addErrorTrace(erFlag, "List of operators", "others");
        return buildTerminalFunctionResponse();
    }

    /**
     * L -> O;L'
     *
     * @return postfix annotation of L -> O L' ;
     */
    private TerminalFunctionResponse LPostfix() {
        TreeNode lNode = buildNonterminalNode("L");
        var oPostfix = OPostfix();
        if (oPostfix.isResult()) {
            lNode.addChild(oPostfix.getNode());
            var lPrimePostfix = LPrimePostfix();
            if (lPrimePostfix.isResult()) {
                lNode.addChild(lPrimePostfix.getNode());
                var a = getCurrentInputSymbol();
                if (a.equals(buildTerminalSemicolon())) {
                    lNode.addChild(buildTerminalNode(a));
                    currentIndex++;
                    return buildTerminalFunctionResponse(lNode);
                } else {
                    incrementFlag();
                    addErrorTrace(erFlag, ";", a.getName());
                    return buildTerminalFunctionResponse();
                }
            }
            incrementFlag();
            addErrorTrace(erFlag, "L'", "Others");
            return buildTerminalFunctionResponse();
        }
        incrementFlag();
        addErrorTrace(erFlag, "O", "other");
        return buildTerminalFunctionResponse();
    }

    /**
     * O -> id := X
     *
     * @return postfix annotation of O -> id X :=
     */
    private TerminalFunctionResponse OPostfix() {
        TreeNode oNode = buildNonterminalNode("O");
        var a = getCurrentInputSymbol();
        if (a.equals(buildTerminalId())) {
            oNode.addChild(buildTerminalNode(a));
            currentIndex++;
            var xPostfix = XPostfix();
            if (xPostfix.isResult()) {
                oNode.addChild(xPostfix.getNode());
                a = getCurrentInputSymbol();
                if (a.equals(buildTerminalIs())) {
                    oNode.addChild(buildTerminalNode(a));
                    currentIndex++;
                    return buildTerminalFunctionResponse(oNode);
                } else {
                    incrementFlag();
                    addErrorTrace(erFlag, ":=", a.getName());
                    return buildTerminalFunctionResponse();
                }
            } else {
                incrementFlag();
                addErrorTrace(erFlag, "X", a.getName());
                return buildTerminalFunctionResponse();
            }
        }
        incrementFlag();
        addErrorTrace(erFlag, "id", a.getName());
        return buildTerminalFunctionResponse();
    }

    /**
     * L' -> O;L' | £
     *
     * @return postfix annotation of L' -> O L' ; | £
     */
    private TerminalFunctionResponse LPrimePostfix() {
        TreeNode lPrimeNode = buildNonterminalNode("L'");
        var oPostfix = OPostfix();
        if (oPostfix.isResult()) {
            lPrimeNode.addChild(oPostfix.getNode());
            currentIndex++;
            var lPrime = LPrimePostfix();
            if (lPrime.isResult()) {
                lPrimeNode.addChild(lPrime.getNode());
                var a = getCurrentInputSymbol();
                if (a.equals(buildTerminalSemicolon())) {
                    lPrimeNode.addChild(buildTerminalNode(a));
                    return buildTerminalFunctionResponse(lPrimeNode);
                }
                incrementFlag();
                addErrorTrace(erFlag, ";", a.getName());
                return buildTerminalFunctionResponse();
            }
            incrementFlag();
            addErrorTrace(erFlag, "L'", "Others");
            return buildTerminalFunctionResponse();
        }
        lPrimeNode.addChild(buildEpsilonNode());
        return buildTerminalFunctionResponse(lPrimeNode);
    }

    /**
     * E' -> A T C
     *
     * @return postfix annotation of E' -> T C A
     */
    private TerminalFunctionResponse EPrimePostfix() {
        TreeNode ePrimeNode = buildNonterminalNode("E'");
        var tcPostfix = TCPostfix(ePrimeNode);
        if (tcPostfix.isResult()) {
            var aPostfix = APostfix();
            if (aPostfix.isResult()) {
                ePrimeNode.addChild(aPostfix.getNode());
                return buildTerminalFunctionResponse(ePrimeNode);
            }
            incrementFlag();
            addErrorTrace(erFlag, "A", "Others ");
            return buildTerminalFunctionResponse();
        }
        incrementFlag();
        addErrorTrace(erFlag, "TC", "Others ");
        return buildTerminalFunctionResponse();

    }

    /**
     * TC -> T C
     *
     * @param node parent's node
     * @return postfix annotation of TC -> T C
     */
    private TerminalFunctionResponse TCPostfix(TreeNode node) {
        var tPostfix = TPostfix();
        if (tPostfix.isResult()) {
            node.addChild(tPostfix.getNode());
            var cPostfix = CPostfix();
            if (cPostfix.isResult()) {
                node.addChild(cPostfix.getNode());
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

    /**
     * T' -> MFD
     *
     * @return postfix annotation of T' -> FDM
     */
    private TerminalFunctionResponse TPrimePostfix() {
        TreeNode tPrimeNode = buildNonterminalNode("T'");
        var fdPostfix = FDPostfix(tPrimeNode);
        if (fdPostfix.isResult()) {
            var mPostfix = MPostfix();
            if (mPostfix.isResult()) {
                tPrimeNode.addChild(mPostfix.getNode());
                return buildTerminalFunctionResponse(tPrimeNode);
            }
            incrementFlag();
            addErrorTrace(erFlag, "M", "Others ");
            return buildTerminalFunctionResponse();
        }
        incrementFlag();
        addErrorTrace(erFlag, "FD", "Others");
        return buildTerminalFunctionResponse();
    }

    /**
     * X -> EX'
     *
     * @return postfix annotation of X -> EX'
     */
    private TerminalFunctionResponse XPostfix() {
        TreeNode xNode = buildNonterminalNode("X");
        var ePostfix = EPostfix();
        if (ePostfix.isResult()) {
            xNode.addChild(ePostfix.getNode());
            var xPrimePostfix = XPrimePostfix();
            if (xPrimePostfix.isResult()) {
                xNode.addChild(xPrimePostfix.getNode());
                return buildTerminalFunctionResponse(xNode);
            }
            incrementFlag();
            addErrorTrace(erFlag, "X'", "Others");

        }
        incrementFlag();
        addErrorTrace(erFlag, "E", "Others");
        return buildTerminalFunctionResponse();
    }

    /**
     * X' -> RE | £
     *
     * @return postfix of X' -> ER | £
     */
    private TerminalFunctionResponse XPrimePostfix() {
        TreeNode xPrimeNode = buildNonterminalNode("X'");
        var ePostfix = EPostfix();
        if (ePostfix.isResult()) {
            xPrimeNode.addChild(ePostfix.getNode());
            var rPostfix = R();
            if (rPostfix.isResult()) {
                xPrimeNode.addChild(rPostfix.getNode());
                return buildTerminalFunctionResponse(xPrimeNode);
            }
            incrementFlag();
            addErrorTrace(erFlag, "R", "Others");
            return buildTerminalFunctionResponse();
        }
        xPrimeNode.addChild(buildEpsilonNode());
        return buildTerminalFunctionResponse(xPrimeNode);
    }

    /**
     * E -> TC
     *
     * @return postfix annotation of E -> TC
     */
    private TerminalFunctionResponse EPostfix() {
        TreeNode eNode = buildNonterminalNode("E");
        return TCPostfix(eNode);
    }

    /**
     * C -> E' | £
     *
     * @return postfix annotation of C -> E' | £
     */
    private TerminalFunctionResponse CPostfix() {
        TreeNode cNode = buildNonterminalNode("C");
        var ePrimePostfix = EPrimePostfix();
        if (ePrimePostfix.isResult()) {
            cNode.addChild(ePrimePostfix.getNode());
            return buildTerminalFunctionResponse(cNode);
        }
        cNode.addChild(buildEpsilonNode());
        return buildTerminalFunctionResponse(cNode);
    }

    /**
     * T -> FD
     *
     * @return postfix annotation of T -> FD
     */
    private TerminalFunctionResponse TPostfix() {
        TreeNode tNode = buildNonterminalNode("T");
        return FDPostfix(tNode);
    }

    /**
     * FD -> F D
     *
     * @param node parent's node
     * @return postfix annotation of FD -> F D
     */
    private TerminalFunctionResponse FDPostfix(TreeNode node) {
        var fPostfix = FPostfix();
        if (fPostfix.isResult()) {
            node.addChild(fPostfix.getNode());
            var dPostfix = DPostfix();
            if (dPostfix.isResult()) {
                node.addChild(dPostfix.getNode());
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

    /**
     * D -> T' | £
     *
     * @return postfix annotation of D -> T' | £
     */
    private TerminalFunctionResponse DPostfix() {
        TreeNode dNode = buildNonterminalNode("D");
        var tPrimePostfix = TPrimePostfix();
        if (tPrimePostfix.isResult()) {
            dNode.addChild(tPrimePostfix.getNode());
            return buildTerminalFunctionResponse(dNode);
        }
        dNode.addChild(buildEpsilonNode());
        return buildTerminalFunctionResponse(dNode);
    }

    /**
     * F -> id | const | (E)
     *
     * @return postfix annotation of F -> id | const | E
     */
    private TerminalFunctionResponse FPostfix() {
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
            currentIndex++;
            fNode.addChild(buildTerminalNode(a));
            var ePostfix = parseE();
            if (ePostfix.isResult()) {
                fNode.addChild(ePostfix.getNode());
                a = getCurrentInputSymbol();
                if (a.equals(buildTerminalRParen())) {
                    fNode.addChild(buildTerminalNode(a));
                    currentIndex++;
                    return buildTerminalFunctionResponse(fNode);
                }
                return buildTerminalFunctionResponse();
            }
            return buildTerminalFunctionResponse();
        }
        incrementFlag();
        addErrorTrace(erFlag, "id , const or E", "Others ");
        return buildTerminalFunctionResponse();
    }

    /**
     * M -> * | /
     *
     * @return postfix annotation of M -> * | /
     */
    private TerminalFunctionResponse MPostfix() {
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

    /**
     * A -> + | -
     *
     * @return postfix annotation of A -> + | -
     */
    private TerminalFunctionResponse APostfix() {
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

    /**
     * R -> < | <= | = | <> | > | >=
     *
     * @return postfix annotation of R -> < | <= | = | <> | > | >=
     */
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

    private TerminalFunctionResponse parseS() {
        var sNode = buildNonterminalNode("S");
        var a = getCurrentInputSymbol();
        if (isBegin(a)) {
            currentIndex++;
            sNode.addChild(buildTerminalNode(a));
            var l = parseL();
            if (l.isResult()) {
                sNode.addChild(l.getNode());
                a = getCurrentInputSymbol();
                if (isEnd(a)) {
                    sNode.addChild(buildTerminalNode(a));
                    return buildTerminalFunctionResponse(sNode);
                }
                return buildTerminalFunctionResponse();
            }
            return buildTerminalFunctionResponse();
        }
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse parseL() {
        var lNode = buildNonterminalNode("L");
        var o = parseO();
        if (o.isResult()) {
            lNode.addChild(o.getNode());
            var a = getCurrentInputSymbol();
            if (a.equals(buildTerminalSemicolon())) {
                lNode.addChild(buildTerminalNode(a));
                while (a.equals(buildTerminalSemicolon())) {
                    currentIndex++;
                    var oSc = parseO();
                    if (oSc.isResult()) {
                        lNode.addChild(oSc.getNode());
                        lNode.addChild(buildTerminalNode(a));
                    }
                    a = getCurrentInputSymbol();
                }
                return buildTerminalFunctionResponse(lNode);
            }
            return buildTerminalFunctionResponse();
        }
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse parseO() {
        var oNode = buildNonterminalNode("O");
        var a = getCurrentInputSymbol();
        if (isId(a)) {
            oNode.addChild(buildTerminalNode(a));
            currentIndex++;
            while (getCurrentInputSymbol().equals(buildTerminalIs())) {
                a = getCurrentInputSymbol();
                currentIndex++;
                var x = parseX();
                if (x.isResult()) {
                    oNode.addChild(x.getNode());
                    oNode.addChild(buildTerminalNode(a));
                } else {
                    return buildTerminalFunctionResponse();
                }
            }
            return buildTerminalFunctionResponse(oNode);
        }
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse parseX() {
        var xNode = buildNonterminalNode("X");
        var e = parseE();
        if (e.isResult()) {
            xNode.addChild(e.getNode());
            var r = parseR();
            while (r.isResult()) {
                var eSc = parseE();
                if (eSc.isResult()) {
                    xNode.addChild(eSc.getNode());
                    xNode.addChild(r.getNode());
                }
                r = parseR();
            }
            return buildTerminalFunctionResponse(xNode);
        }
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse parseE() {
        var eNode = buildNonterminalNode("E");
        var t = parseT();
        if (t.isResult()) {
            eNode.addChild(t.getNode());
            var a = parseA();
            while (a.isResult()) {
                var tSc = parseT();
                if (tSc.isResult()) {
                    eNode.addChild(tSc.getNode());
                    eNode.addChild(a.getNode());
                }
                a = parseA();
            }
            return buildTerminalFunctionResponse(eNode);
        }
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse parseA() {
        return APostfix();
    }

    private TerminalFunctionResponse parseR() {
        return R();
    }

    private TerminalFunctionResponse parseM() {
        return MPostfix();
    }

    private TerminalFunctionResponse parseT() {
        var tNode = buildNonterminalNode("T");
        var f = parseF();
        if (f.isResult()) {
            tNode.addChild(f.getNode());
            var m = parseM();
            while (m.isResult()) {
                var fSc = parseF();
                if (fSc.isResult()) {
                    tNode.addChild(fSc.getNode());
                    tNode.addChild(m.getNode());
                }
                m = parseM();
            }
            return buildTerminalFunctionResponse(tNode);
        }
        return buildTerminalFunctionResponse();
    }

    private TerminalFunctionResponse parseF() {
        var fNode = buildNonterminalNode("F");
        var a = getCurrentInputSymbol();
        if (isId(a)) {
            fNode.addChild(buildTerminalNode(a));
            currentIndex++;
            return buildTerminalFunctionResponse(fNode);
        }
        if (isConstant(a)) {
            fNode.addChild(buildTerminalNode(a));
            currentIndex++;
            return buildTerminalFunctionResponse(fNode);
        }

        if (a.equals(buildTerminalLParen())) {
            currentIndex++;
            var e = parseE();
            if (e.isResult()) {
                fNode.addChild(e.getNode());
                a = getCurrentInputSymbol();
                if (a.equals(buildTerminalRParen())) {
                    currentIndex++;
                    return buildTerminalFunctionResponse(fNode);
                }
                return buildTerminalFunctionResponse();
            }
            return buildTerminalFunctionResponse();
        }
        return buildTerminalFunctionResponse();
    }

    private void incrementFlag() {
        erFlag++;
    }

    public boolean parse() {
        currentIndex = 0;
        erFlag = 0;
        TerminalFunctionResponse response = parseS();
        root = response.getNode();
        if (response.isResult()) {
            //sb(root);
            return true;
        } else {
            if (erFlag > 0) {
                System.out.println("INTERNAL ERROR");
                System.out.println("Not expected \'" + getCurrentInputSymbol().getName() + "\' at " + currentIndex + " position ");
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
