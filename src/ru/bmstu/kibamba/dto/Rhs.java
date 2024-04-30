package ru.bmstu.kibamba.dto;

import java.util.List;

public class Rhs {
    private List<Symbol> symbol;

    public Rhs(List<Symbol> symbol) {
        this.symbol = symbol;
    }

    public List<Symbol> getSymbol() {
        return symbol;
    }

    public void setSymbol(List<Symbol> symbol) {
        this.symbol = symbol;
    }
}
