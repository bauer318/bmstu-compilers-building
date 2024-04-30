package ru.bmstu.kibamba.grammars;

import java.util.*;

public class Grammar implements Cloneable {
    private Set<String> nonterminals;
    private Set<String> terminals;
    private List<Production> productions;
    private String firstSymbol;

    public Grammar(Set<String> nonterminals, Set<String> terminals, List<Production> productions, String firstSymbol) {
        this.nonterminals = nonterminals;
        this.terminals = terminals;
        this.productions = productions;
        this.firstSymbol = firstSymbol;
    }


    public Set<String> getNonterminals() {
        return nonterminals;
    }

    public void setNonterminals(Set<String> nonterminals) {
        this.nonterminals = nonterminals;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public void setTerminals(Set<String> terminals) {
        this.terminals = terminals;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public void setProductions(List<Production> productions) {
        this.productions = productions;
    }

    public String getFirstSymbol() {
        return firstSymbol;
    }

    public void setFirstSymbol(String firstSymbol) {
        this.firstSymbol = firstSymbol;
    }

    @Override
    public Grammar clone() {
        try {
            Grammar clone = (Grammar) super.clone();
            clone.setNonterminals(new LinkedHashSet<>(this.nonterminals));
            clone.setFirstSymbol(this.firstSymbol);
            clone.setProductions(new ArrayList<>(this.productions));
            clone.setTerminals(new LinkedHashSet<>(this.terminals));
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.nonterminals.toString()).append("\n");
        sb.append(this.terminals.toString()).append("\n");
        sb.append(this.productions.toString()).append("\n");
        sb.append(this.firstSymbol);
        return sb.toString();
    }
}
