package ru.bmstu.kibamba.models;

import java.util.Objects;

public class Nonterminal extends GrammarSymbol implements Cloneable {
    private boolean isStartSymbol;

    public Nonterminal(String name, String attribute, boolean isStartSymbol) {
        super(name, attribute);
        this.isStartSymbol = isStartSymbol;
    }

    public Nonterminal(String name, String attribute) {
        super(name, attribute);
        this.isStartSymbol = false;
    }

    public boolean isStartSymbol() {
        return isStartSymbol;
    }

    public void setStartSymbol(boolean isStartSymbol) {
        this.isStartSymbol = isStartSymbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Nonterminal that = (Nonterminal) o;
        return isStartSymbol == that.isStartSymbol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), isStartSymbol);
    }

    @Override
    public Nonterminal clone() {
        Nonterminal clone = (Nonterminal) super.clone();
        clone.isStartSymbol = this.isStartSymbol;
        return clone;
    }
}
