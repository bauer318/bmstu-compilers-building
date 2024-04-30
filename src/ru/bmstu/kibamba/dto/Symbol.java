package ru.bmstu.kibamba.dto;

public class Symbol {
    private SymbolType type;
    private String name;

    public Symbol(String type, String name) {
        setType(type);
        setName(name);
    }

    public SymbolType getType() {
        return type;
    }

    public void setType(String type) {
        if (type.equals(type.toUpperCase())) {
            this.type = SymbolType.NONTERM;
        } else {
            this.type = SymbolType.TERM;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.equals(name.toUpperCase())) {
            this.name = name;
        } else {
            this.name = getTermName(name);
        }

    }

    private String getTermName(String name) {
        switch (name) {
            case "+":
                return TermType.ADD.name();
            case "-":
                return TermType.SUB.name();
            case "/":
                return TermType.DIV.name();
            case "*":
                return TermType.MUL.name();
            case "(":
                return TermType.LPAREN.name();
            case ")":
                return TermType.RPAREN.name();
            default:
                return TermType.IDENT.name();
        }
    }
}
