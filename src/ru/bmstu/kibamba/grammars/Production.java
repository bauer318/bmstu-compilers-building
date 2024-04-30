package ru.bmstu.kibamba.grammars;

import java.util.Objects;

public class Production implements Cloneable {
    private String nonterminal;
    private String chain;

    public Production(String nonterminal){
        this.setNonterminal(nonterminal);
        this.chain = "";
    }

    public Production(String nonterminal, String chain){
        this.setNonterminal(nonterminal);
        this.chain = chain;
    }

    public void setNonterminal(String nonterminal){
        var upperCaseNonTerminal = String.valueOf(nonterminal).toUpperCase();
        this.nonterminal = upperCaseNonTerminal;
    }

    public void setChain(String chain){
        this.chain = chain;
    }

    public String getNonterminal(){
        return this.nonterminal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Production that = (Production) o;
        return Objects.equals(nonterminal, that.nonterminal) && Objects.equals(chain, that.chain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nonterminal, chain);
    }

    public String getChain(){
        return this.chain;
    }

    @Override
    public String toString(){
        return this.nonterminal + "->" + this.chain;
    }

    @Override
    public Production clone() {
        try {
            Production clone = (Production) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
