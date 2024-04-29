package ru.bmstu.kibamba.grammar;

import java.util.Objects;

public class Production {
    private String noTerminal;
    private String chain;

    public Production(String noTerminal){
        this.setNoTerminal(noTerminal);
        this.chain = "";
    }

    public Production(String noTerminal, String chain){
        this.setNoTerminal(noTerminal);
        this.chain = chain;
    }

    public void setNoTerminal(String noTerminal){
        var upperCaseNonTerminal = String.valueOf(noTerminal).toUpperCase();
        this.noTerminal = upperCaseNonTerminal;
    }

    public void setChain(String chain){
        this.chain = chain;
    }

    public String getNoTerminal(){
        return this.noTerminal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Production that = (Production) o;
        return Objects.equals(noTerminal, that.noTerminal) && Objects.equals(chain, that.chain);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noTerminal, chain);
    }

    public String getChain(){
        return this.chain;
    }

    @Override
    public String toString(){
        return this.noTerminal + "->" + this.chain;
    }
}
