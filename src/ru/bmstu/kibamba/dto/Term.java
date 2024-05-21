package ru.bmstu.kibamba.dto;

public class Term {
    private TermType name;
    private String spell;

    public Term(String name, String spell) {
        this.setName(name);
        this.spell = spell;
    }

    public TermType getName() {
        return name;
    }

    public void setName(String name) {
        switch (name) {
            case "+":
                this.name = TermType.ADD;
                break;
            case "-":
                this.name = TermType.SUB;
                break;
            case "/":
                this.name = TermType.DIV;
                break;
            case "*":
                this.name = TermType.MUL;
                break;
            case "(":
                this.name = TermType.LPAREN;
                break;
            case ")":
                this.name = TermType.RPAREN;
                break;
            case "const":
                this.name = TermType.CONST;
                break;
            case "begin":
                this.name = TermType.BEGIN;
                break;
            case "end":
                this.name = TermType.END;
                break;
            case "<":
                this.name = TermType.L;
                break;
            case "<=":
                this.name = TermType.LE;
                break;
            case "=":
                this.name = TermType.E;
                break;
            case "<>":
                this.name = TermType.NE;
                break;
            case ">":
                this.name = TermType.G;
                break;
            case ">=":
                this.name = TermType.GE;
                break;
            case ";":
                this.name = TermType.SEMICOLON;
                break;
            case ":=":
                this.name = TermType.IS;
                break;
            default:
                this.name = TermType.IDENT;
        }
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }
}
