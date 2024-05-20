package ru.bmstu.kibamba.models;

import java.util.Set;

public class ProductionChain {
    private Set<GrammarSymbol> chain;

    public ProductionChain(Set<GrammarSymbol> chain) {
        this.chain = chain;
    }

    public Set<GrammarSymbol> getChain() {
        return chain;
    }

    public void setChain(Set<GrammarSymbol> chain) {
        this.chain = chain;
    }
}
