package ru.bmstu.kibamba.parsing;

import ru.bmstu.kibamba.models.Grammar;
import ru.bmstu.kibamba.models.GrammarSymbol;
import ru.bmstu.kibamba.models.Terminal;

public class TerminalBuilder {

    public static Terminal buildTerminalConst() {
        return new Terminal("const", "val", "const");
    }

    public static Terminal buildTerminalConst(String constValue) {
        var terminal = buildTerminalConst();
        terminal.setValue(constValue);
        return terminal;
    }

    public static boolean isConstant(GrammarSymbol symbol) {
        return symbol.getName().equals("const");
    }

    public static boolean isId(GrammarSymbol symbol) {
        return symbol.getName().equals("id");
    }

    public static boolean isBegin(GrammarSymbol symbol) {
        return symbol.getValue().equals("begin");
    }

    public static boolean isEnd(GrammarSymbol symbol) {
        return symbol.getName().equals("end");
    }

    public static Terminal buildTerminalLParen() {
        return new Terminal("(", "", "lParen");
    }

    public static Terminal buildTerminalRParen() {
        return new Terminal(")", "", "rParen");
    }

    public static Terminal buildTerminalMul() {
        return new Terminal("*", "", "MUL");
    }

    public static Terminal buildTerminalDiv() {
        return new Terminal("/", "", "DIV");
    }

    public static Terminal buildTerminalAdd() {
        return new Terminal("+", "", "ADD");
    }

    public static Terminal buildTerminalSub() {
        return new Terminal("-", "", "SUB");
    }

    public static Terminal buildTerminalLess() {
        return new Terminal("<", "", "L");
    }

    public static Terminal buildTerminalLessEqual() {
        return new Terminal("<=", "", "LE");
    }

    public static Terminal buildTerminalEqual() {
        return new Terminal("=", "", "E");
    }

    public static Terminal buildTerminalNotEqual() {
        return new Terminal("<>", "", "NE");
    }

    public static Terminal buildTerminalGreat() {
        return new Terminal(">", "", "G");
    }

    public static Terminal buildTerminalGreatEqual() {
        return new Terminal(">=", "", "GE");
    }

    public static Terminal buildTerminalSemicolon() {
        return new Terminal(";", "", "semicolon");
    }

    public static Terminal buildTerminalBegin() {
        return new Terminal("begin", "", "begin");
    }

    public static Terminal buildTerminalBegin(String terminalValue) {
        var terminal = buildTerminalBegin();
        terminal.setValue(terminalValue);
        return terminal;
    }

    public static Terminal buildTerminalEnd() {
        return new Terminal("end", "", "end");
    }

    public static Terminal buildTerminalEnd(String terminalValue) {
        var terminal = buildTerminalEnd();
        terminal.setValue(terminalValue);
        return terminal;
    }

    public static Terminal buildTerminalId() {
        return new Terminal("id", "val", "id");
    }

    public static Terminal buildTerminalId(String terminalValue) {
        var terminal = buildTerminalId();
        terminal.setValue(terminalValue);
        return terminal;
    }

    public static Terminal buildTerminalIs() {
        return new Terminal(":=", "", "is");
    }
}
