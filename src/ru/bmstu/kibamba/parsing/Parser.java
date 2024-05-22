package ru.bmstu.kibamba.parsing;

import ru.bmstu.kibamba.models.Grammar;
import ru.bmstu.kibamba.models.GrammarSymbol;
import ru.bmstu.kibamba.models.Terminal;

import java.util.List;

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

    public GrammarSymbol next() {
        return input.get(currentIndex);
    }

    private boolean S() {
        root = new TreeNode(grammar.getStart().getName());
        var a = next();
        if (a.equals(new Terminal("begin", "begin"))) {
            currentIndex++;
            root.addChild(new TreeNode("begin"));
            if (L()) {
                a = next();
                if (a.equals(new Terminal("end", "end"))) {
                    root.addChild(new TreeNode("end"));
                    return true;
                } else {
                    erFlag++;
                    printError(1, "end", a.getName());
                    return false;
                }
            } else {
                printError(2, "L", a.getName());
            }
        }
        return false;
    }

    private boolean L() {
        TreeNode lNode = new TreeNode("L");
        var a = next();
        if (O()) {
            lNode.addChild(new TreeNode("O"));
            if (B()) {
                lNode.addChild(new TreeNode("B"));
                root.addChild(lNode);
                return true;
            } else {
                printError(3, " B after O ", a.getName());
                return false;
            }
        }
        printError(4, "O", a.getName());
        return false;
    }

    private boolean B() {
        TreeNode bNode = new TreeNode("B");
        root.addChild(bNode);
        if (LPrime()) {
            bNode.addChild(new TreeNode("L'"));
            return true;
        }
        bNode.addChild(new TreeNode("£"));
        return true;
    }

    private boolean O() {
        TreeNode oNode = new TreeNode("O");
        var a = next();
        if (a.equals(new Terminal("var", "var"))) {
            oNode.addChild(new TreeNode("var"));
            currentIndex++;
            a = next();
            if (a.equals(new Terminal(":=", "is"))) {
                oNode.addChild(new TreeNode(":="));
                currentIndex++;
                if (X()) {
                    oNode.addChild(new TreeNode("X"));
                    root.addChild(oNode);
                    return true;
                } else {
                    printError(6, "X", a.getName());
                    return false;
                }
            } else {
                printError(7, ":=", a.getName());
                return false;
            }
        }
        printError(8, "var", a.getName());
        return false;
    }

    private boolean LPrime() {
        TreeNode lPrimeNode = new TreeNode("L'");

        var a = next();
        if (a.equals(new Terminal(";", "semicolon"))) {
            lPrimeNode.addChild(new TreeNode(";"));
            currentIndex++;
            if (O()) {
                lPrimeNode.addChild(new TreeNode("O"));
                if (B()) {
                    lPrimeNode.addChild(new TreeNode("B"));
                    root.addChild(lPrimeNode);
                    return true;
                } else {
                    printError(9, "B", a.getName());
                    return false;
                }
            } else {
                printError(10, "O", a.getName());
                return false;
            }
        }
        printError(11, ";", a.getName());
        return false;
    }

    private boolean EPrime() {
        TreeNode ePrimeNode = new TreeNode("E'");

        if (A()) {
            ePrimeNode.addChild(new TreeNode("A"));
            if (T()) {
                ePrimeNode.addChild(new TreeNode("T"));
                if (C()) {
                    ePrimeNode.addChild(new TreeNode("C"));
                    root.addChild(ePrimeNode);
                    return true;
                }
                printError(23, "C", "Others ");
                return false;
            }
            printError(24, "T", "Others ");
            return false;
        }
        printError(25, "A", "Others ");
        return false;
    }

    private boolean TPrime() {
        TreeNode tPrimeNode = new TreeNode("T'");

        if (M()) {
            tPrimeNode.addChild(new TreeNode("M"));
            if (F()) {
                tPrimeNode.addChild(new TreeNode("F"));
                if (D()) {
                    tPrimeNode.addChild(new TreeNode("D"));
                    root.addChild(tPrimeNode);
                    return true;
                }
                printError(27, "D", "Others ");
                return false;
            }
            printError(28, "F", "Others ");
            return false;
        }
        printError(29, "M", "Others ");
        return false;
    }

    private boolean X() {
        TreeNode xNode = new TreeNode("X");
        var a = next();
        if (E()) {
            xNode.addChild(new TreeNode("E"));
            if (XPrime()) {
                xNode.addChild(new TreeNode("X'"));
                root.addChild(xNode);
                return true;
            }
            printError(12, "X'", a.getName());

        }
        printError(13, "E", a.getName());
        return false;
    }

    private boolean XPrime() {
        TreeNode xPrimeNode = new TreeNode("X'");
        if (R()) {
            xPrimeNode.addChild(new TreeNode("R"));
            if (E()) {
                xPrimeNode.addChild(new TreeNode("E"));
                root.addChild(xPrimeNode);
                return true;
            }
            printError(14, "E", "Others ");
            return false;
        }
        xPrimeNode.addChild(new TreeNode("£"));
        root.addChild(xPrimeNode);
        return true;
    }

    private boolean E() {
        TreeNode eNode = new TreeNode("E");
        if (T()) {
            eNode.addChild(new TreeNode("T"));
            if (C()) {
                eNode.addChild(new TreeNode("C"));
                root.addChild(eNode);
                return true;
            }
            printError(15, "C", "Others ");
            return false;
        }
        printError(16, "T", "Others ");
        return false;
    }

    private boolean C() {
        TreeNode cNode = new TreeNode("C");
        root.addChild(cNode);
        if (EPrime()) {
            cNode.addChild(new TreeNode("E'"));
            return true;
        }
        cNode.addChild(new TreeNode("£"));
        return true;
    }

    private boolean T() {
        TreeNode tNode = new TreeNode("T");
        if (F()) {
            tNode.addChild(new TreeNode("F"));
            if (D()) {
                tNode.addChild(new TreeNode("D"));
                root.addChild(tNode);
                return true;
            }
            printError(17, "D", "Others ");
            return false;
        }
        printError(18, "F", "Others ");
        return false;
    }

    private boolean D() {
        TreeNode dNode = new TreeNode("D");
        if (TPrime()) {
            dNode.addChild(new TreeNode("T'"));
            root.addChild(dNode);
            return true;
        }
        dNode.addChild(new TreeNode("£"));
        return true;
    }

    private boolean F() {
        TreeNode fNode = new TreeNode("F");

        var a = next();
        if (a.equals(new Terminal("var", "var"))) {
            currentIndex++;
            fNode.addChild(new TreeNode("var"));
            root.addChild(fNode);
            return true;
        }

        if (a.equals(new Terminal("const", "const"))) {
            currentIndex++;
            fNode.addChild(new TreeNode("const"));
            root.addChild(fNode);
            return true;
        }

        if (a.equals(new Terminal("(", "lParen"))) {
            fNode.addChild(new TreeNode("("));
            currentIndex++;
            if (E()) {
                fNode.addChild(new TreeNode("E"));
                a = next();
                if (a.equals(new Terminal(")", "rParen"))) {
                    currentIndex++;
                    fNode.addChild(new TreeNode(")"));
                    root.addChild(fNode);
                    return true;
                }
                printError(19, ")", a.getName());
                return false;
            }
            printError(20, "E", "Others ");
            return false;
        }

        printError(21, "var , const or (E)", "Others ");
        return false;
    }

    private boolean M() {
        var a = next();
        TreeNode mNode = new TreeNode("M");

        if (a.equals(new Terminal("*", "MUL"))) {
            currentIndex++;
            mNode.addChild(new TreeNode("*"));
            root.addChild(mNode);
            return true;
        } else if (a.equals(new Terminal("/", "DIV"))) {
            currentIndex++;
            mNode.addChild(new TreeNode("/"));
            root.addChild(mNode);
            return true;
        }
        printError(1, "* or /", a.getName());
        return false;
    }

    private boolean A() {
        var a = next();
        TreeNode aNode = new TreeNode("A");
        root.addChild(aNode);
        if (a.equals(new Terminal("+", "ADD"))) {
            currentIndex++;
            aNode.addChild(new TreeNode("+"));
            return true;
        } else if (a.equals(new Terminal("-", "SUB"))) {
            currentIndex++;
            aNode.addChild(new TreeNode("-"));
            return true;
        }
        printError(2, "+ or -", a.getName());
        return false;
    }

    private boolean R() {
        var a = next();
        TreeNode rNode = new TreeNode("R");

        if (a.equals(new Terminal("<", "L"))) {
            currentIndex++;
            rNode.addChild(new TreeNode("<"));
            root.addChild(rNode);
            return true;
        } else if (a.equals(new Terminal("<=", "LE"))) {
            currentIndex++;
            rNode.addChild(new TreeNode("<="));
            root.addChild(rNode);
            return true;
        } else if (a.equals(new Terminal("=", "E"))) {
            currentIndex++;
            rNode.addChild(new TreeNode("="));
            root.addChild(rNode);
            return true;
        } else if (a.equals(new Terminal("<>", "NE"))) {
            currentIndex++;
            rNode.addChild(new TreeNode("<>"));
            root.addChild(rNode);
            return true;
        } else if (a.equals(new Terminal(">", "G"))) {
            currentIndex++;
            rNode.addChild(new TreeNode(">"));
            root.addChild(rNode);
            return true;
        } else if (a.equals(new Terminal(">=", "GE"))) {
            currentIndex++;
            rNode.addChild(new TreeNode(">="));
            root.addChild(rNode);
            return true;
        }
        printError(22, "<, >, <=, >=, =,<>", a.getName());
        return false;
    }

    private void printError(int errorNumber, String expected, String found) {
        System.out.println("ERROR " + errorNumber + " expected " + expected + " but found " + found);
    }

    public boolean parseS() {
        currentIndex = 0;
        erFlag = 0;
        if (S()) {
            return true;
        }
        return false;
    }

    public TreeNode getRoot() {
        return root;
    }
}
