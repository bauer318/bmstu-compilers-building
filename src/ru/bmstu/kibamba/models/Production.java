package ru.bmstu.kibamba.models;

public class Production {
    private Nonterminal nonterminal;
    private ProductionChain chain;

    public Production(Nonterminal nonterminal, ProductionChain chain) {
        this.nonterminal = nonterminal;
        this.chain = chain;
    }

    public Nonterminal getNonterminal() {
        return nonterminal;
    }

    public void setNonterminal(Nonterminal nonterminal) {
        this.nonterminal = nonterminal;
    }

    public ProductionChain getChain() {
        return chain;
    }

    public void setChain(ProductionChain chain) {
        this.chain = chain;
    }
}
