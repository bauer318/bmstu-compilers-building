package ru.bmstu.kibamba.parsing;

import ru.bmstu.kibamba.models.Terminal;

public class TerminalBuilder {

    public static Terminal buildTerminalConst() {
        return new Terminal("const", "const");
    }

    public static Terminal buildTerminalLParen() {
        return new Terminal("(", "lParen");
    }

    public static Terminal buildTerminalRParen() {
        return new Terminal(")", "rParen");
    }

    public static Terminal buildTerminalMul() {
        return new Terminal("*", "MUL");
    }

    public static Terminal buildTerminalDiv() {
        return new Terminal("/", "DIV");
    }

    public static Terminal buildTerminalAdd() {
        return new Terminal("+", "ADD");
    }

    public static Terminal buildTerminalSub() {
        return new Terminal("-", "SUB");
    }

    public static Terminal buildTerminalLess() {
        return new Terminal("<", "L");
    }

    public static Terminal buildTerminalLessEqual() {
        return new Terminal("<=", "LE");
    }

    public static Terminal buildTerminalEqual() {
        return new Terminal("=", "E");
    }

    public static Terminal buildTerminalNotEqual() {
        return new Terminal("<>", "NE");
    }

    public static Terminal buildTerminalGreat() {
        return new Terminal(">", "G");
    }

    public static Terminal buildTerminalGreatEqual() {
        return new Terminal(">=", "GE");
    }

    public static Terminal buildTerminalSemicolon() {
        return new Terminal(";", "semicolon");
    }

    public static Terminal buildTerminalBegin() {
        return new Terminal("begin", "begin");
    }

    public static Terminal buildTerminalEnd() {
        return new Terminal("end", "end");
    }

    public static Terminal buildTerminalId() {
        return new Terminal("id", "id");
    }

    public static Terminal buildTerminalIs() {
        return new Terminal(":=", "is");
    }
}
