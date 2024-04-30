package ru.bmstu.kibamba.dto;

public class Production {
    private Lhs lhs;
    private Rhs rhs;

    public Production(Lhs lhs, Rhs rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Lhs getLhs() {
        return lhs;
    }

    public void setLhs(Lhs lhs) {
        this.lhs = lhs;
    }

    public Rhs getRhs() {
        return rhs;
    }

    public void setRhs(Rhs rhs) {
        this.rhs = rhs;
    }
}
