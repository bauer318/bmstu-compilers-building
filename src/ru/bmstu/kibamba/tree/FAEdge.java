package ru.bmstu.kibamba.tree;

import java.util.Objects;

public class FAEdge {
    private int state;
    private char symbol;

    public FAEdge(int state, char symbol) {
        this.state = state;
        this.symbol = symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FAEdge)) return false;
        FAEdge faEdge = (FAEdge) o;
        return state == faEdge.state && symbol == faEdge.symbol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, symbol);
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
}
