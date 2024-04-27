package ru.bmstu.kibamba.gramatic;

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

    public String getChain(){
        return this.chain;
    }

    @Override
    public String toString(){
        return this.noTerminal + "->" + this.chain;
    }
}
