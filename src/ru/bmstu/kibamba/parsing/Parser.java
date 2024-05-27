package ru.bmstu.kibamba.parsing;

import ru.bmstu.kibamba.dto.TerminalFunctionResponse;
import ru.bmstu.kibamba.models.Grammar;
import ru.bmstu.kibamba.models.GrammarSymbol;

import java.util.ArrayList;
import java.util.List;

import static ru.bmstu.kibamba.parsing.ParserUtils.*;
import static ru.bmstu.kibamba.parsing.TerminalBuilder.*;

public class Parser {
    private final List<GrammarSymbol> input;
    private int currentIndex;
    private TreeNode root;
    private int erFlag;
    private final List<String> errorsTrace = new ArrayList<>();

    public Parser(List<GrammarSymbol> input) {
        this.input = input;
    }

    public GrammarSymbol getCurrentInputSymbol() {
        return input.get(currentIndex);
    }

    private void addErrorTrace(int errorNumber, String expected, String found) {
        errorsTrace.add("ERROR " + errorNumber + " expected " + expected + " but found " + found + "\n");
    }

    /**
     * S -> begin L end
     *
     * @return postfix annotation of S -> begin L end
     */
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
                incrementFlag();
                return buildTerminalFunctionResponse();
            }
            incrementFlag();
            return buildTerminalFunctionResponse();
        }
        incrementFlag();
        return buildTerminalFunctionResponse();
    }

    /**
     * L -> O; | L; O;
     *
     * @return postfix annotation of L -> O; | LO; ;
     */
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
                        a = getCurrentInputSymbol();
                        if (a.equals(buildTerminalSemicolon())) {
                            lNode.addChild(oSc.getNode());
                            lNode.addChild(buildTerminalNode(a));
                            a = getCurrentInputSymbol();
                        } else {
                            return buildTerminalFunctionResponse();
                        }
                    } else {
                        a = getCurrentInputSymbol();
                        if (isEnd(a)) {
                            return buildTerminalFunctionResponse(lNode);
                        }
                        return buildTerminalFunctionResponse();
                    }
                }
                return buildTerminalFunctionResponse(lNode);
            }
            incrementFlag();
            return buildTerminalFunctionResponse();
        }
        incrementFlag();
        return buildTerminalFunctionResponse();
    }

    /**
     * O -> id := X
     *
     * @return postfix annotation of O -> id X :=
     */
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
                    incrementFlag();
                    return buildTerminalFunctionResponse();
                }
            }
            return buildTerminalFunctionResponse(oNode);
        }
        incrementFlag();
        return buildTerminalFunctionResponse();
    }

    /**
     * X -> ERE|E
     *
     * @return postfix annotation of X -> EER|E
     */
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
        incrementFlag();
        return buildTerminalFunctionResponse();
    }

    /**
     * E -> EAT | T
     *
     * @return postfix annotation of E -> ETA | T
     */
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
        incrementFlag();
        return buildTerminalFunctionResponse();
    }

    /**
     * A -> + | -
     *
     * @return postfix annotation of A -> +|-
     */
    private TerminalFunctionResponse parseA() {
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
    private TerminalFunctionResponse parseR() {
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

    /**
     * M -> * | /
     *
     * @return postfix annotation of M -> * | /
     */
    private TerminalFunctionResponse parseM() {
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
     * T -> TMF | T
     *
     * @return postfix annotation of T -> TFM | T
     */
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
        incrementFlag();
        return buildTerminalFunctionResponse();
    }

    /**
     * F -> id | const | (E)
     *
     * @return postfix annotation of F -> id | const | E
     */
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
                incrementFlag();
                return buildTerminalFunctionResponse();
            }
            incrementFlag();
            return buildTerminalFunctionResponse();
        }
        incrementFlag();
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
            return true;
        } else {
            if (erFlag > 0) {
                System.out.println("INTERNAL ERROR");
                System.out.println("Not expected '" + getCurrentInputSymbol().getName() + "' at " + currentIndex + " position ");
            } else {
                System.out.println("Position " + currentIndex);
                System.out.println("Error: Incorrect  first symbol of S!");
            }
        }
        return false;
    }

    public TreeNode getRoot() {
        return root;
    }
}
