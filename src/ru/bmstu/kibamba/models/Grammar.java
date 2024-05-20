package ru.bmstu.kibamba.models;

import java.util.Set;

public class Grammar {
    private Set<Nonterminal> nonterminals;
    private Set<Terminal> terminals;
    private Nonterminal start;
    private Set<Production> productions;

    public Grammar(Set<Nonterminal> nonterminals,
                   Set<Terminal> terminals,
                   Nonterminal start,
                   Set<Production> productions) {
        this.nonterminals = nonterminals;
        this.terminals = terminals;
        this.start = start;
        this.productions = productions;
    }

    public Set<Nonterminal> getNonterminals() {
        return nonterminals;
    }

    public void setNonterminals(Set<Nonterminal> nonterminals) {
        this.nonterminals = nonterminals;
    }

    public Set<Terminal> getTerminals() {
        return terminals;
    }

    public void setTerminals(Set<Terminal> terminals) {
        this.terminals = terminals;
    }

    public Nonterminal getStart() {
        return start;
    }

    public void setStart(Nonterminal start) {
        this.start = start;
    }

    public Set<Production> getProductions() {
        return productions;
    }

    public void setProductions(Set<Production> productions) {
        this.productions = productions;
    }
}
