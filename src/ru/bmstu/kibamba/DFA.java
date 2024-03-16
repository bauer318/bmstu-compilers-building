package ru.bmstu.kibamba;

import java.util.LinkedList;

public class DFA {
    private LinkedList<State> dfa;

    public DFA(){
        this.dfa = new LinkedList<>();
    }

    public LinkedList<State> getDfa() {
        return dfa;
    }

    public void setDfa(LinkedList<State> dfa) {
        this.dfa = dfa;
    }
}
