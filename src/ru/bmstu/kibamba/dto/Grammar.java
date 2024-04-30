package ru.bmstu.kibamba.dto;

import java.util.List;

public class Grammar {
    private String name;
    private List<Term> terminalSymbols;
    private List<Nonterm> nonterminalSymbols;
    private List<Production> productions;
    private StartSymbol startSymbol;

    public Grammar(String name, List<Term> terminalSymbols,
                   List<Nonterm> nonterminalSymbols,
                   List<Production> productions,
                   StartSymbol startSymbol) {
        this.name = name;
        this.terminalSymbols = terminalSymbols;
        this.nonterminalSymbols = nonterminalSymbols;
        this.productions = productions;
        this.startSymbol = startSymbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Term> getTerminalSymbols() {
        return terminalSymbols;
    }

    public void setTerminalSymbols(List<Term> terminalSymbols) {
        this.terminalSymbols = terminalSymbols;
    }

    public List<Nonterm> getNonterminalSymbols() {
        return nonterminalSymbols;
    }

    public void setNonterminalSymbols(List<Nonterm> nonterminalSymbols) {
        this.nonterminalSymbols = nonterminalSymbols;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public void setProductions(List<Production> productions) {
        this.productions = productions;
    }

    public StartSymbol getStartSymbol() {
        return startSymbol;
    }

    public void setStartSymbol(StartSymbol startSymbol) {
        this.startSymbol = startSymbol;
    }
}
