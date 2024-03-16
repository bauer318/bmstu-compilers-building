package ru.bmstu.kibamba;

import java.util.LinkedList;

public class NFA {
    private LinkedList<State> nfa;

    public NFA() {
        this.nfa = new LinkedList<>();
    }

    public LinkedList<State> getNfa() {
        return nfa;
    }

    public void setNfa(LinkedList<State> nfa) {
        this.nfa = nfa;
    }
}
