package ru.bmstu.kibamba;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class State {
    private int id;
    private Map<Character, ArrayList<State>> nextState;
    private Set<State> states;

    private boolean acceptState;

    public State(int id) {
        this.id = id;
        this.nextState = new HashMap<>();
        this.acceptState = false;
    }

    public State(int id, Set<State> states) {
        this.id = id;
        this.states = states;
        this.nextState = new HashMap<>();

        for (State state : states) {
            if (state.isAcceptState()) {
                this.acceptState = true;
                break;
            }
        }
    }

    public void addTransition(State next, char key) {
        ArrayList<State> states = this.nextState.computeIfAbsent(key, k -> new ArrayList<>());
        states.add(next);
    }

    public ArrayList<State> getAllTransitions(char c) {
        if (this.nextState.get(c) == null) {
            return new ArrayList<>();
        }
        return this.nextState.get(c);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Character, ArrayList<State>> getNextState() {
        return nextState;
    }

    public void setNextState(Map<Character, ArrayList<State>> nextState) {
        this.nextState = nextState;
    }

    public Set<State> getStates() {
        return states;
    }

    public void setStates(Set<State> states) {
        this.states = states;
    }

    public boolean isAcceptState() {
        return acceptState;
    }

    public void setAcceptState(boolean acceptState) {
        this.acceptState = acceptState;
    }
}
