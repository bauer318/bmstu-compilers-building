package ru.bmstu.kibamba.tree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DFA {
    private final ArrayList<Transition> transitions = new ArrayList<>();
    private final ArrayList<ArrayList<Integer>> entries = new ArrayList<>();
    private final ArrayList<Boolean> marked = new ArrayList<>();
    private final ArrayList<Integer> finalStates = new ArrayList<>();

    /**
     * Add newly_created entry into DFA
     */
    public int addEntry(ArrayList<Integer> entry) {
        entries.add(entry);
        marked.add(false);
        return entries.size() - 1;
    }

    /**
     * Return the array position of the next unmarked entry
     */
    public int nextUnmarkedEntryIdx() {
        for (int i = 0; i < marked.size(); i++) {
            if (!marked.get(i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * mark the entry specified by index as marked (marked = true)
     */
    public void markEntry(int idx) {
        marked.set(idx, true);
    }

    public ArrayList<Integer> entryAt(int i) {
        return entries.get(i);
    }

    public int findEntry(ArrayList<Integer> entry) {
        for (int i = 0; i < entries.size(); i++) {
            ArrayList<Integer> it = entries.get(i);
            if (it.equals(entry)) {
                return i;
            }
        }
        return -1;
    }

    public void setFinalState(int nfaFinalState) {
        for (int i = 0; i < entries.size(); i++) {
            ArrayList<Integer> entry = entries.get(i);
            for (int state : entry) {
                if (state == nfaFinalState) {
                    finalStates.add(i);
                }
            }
        }
    }

    public void setMinDfaFinalState(int minDfaFinalState) {
        this.finalStates.add(minDfaFinalState);
    }

    public void setTransition(int fromState, int toState, char symbol) {
        Transition newTransition = new Transition(fromState, toState, symbol);
        transitions.add(newTransition);
    }

    public ArrayList<Integer> getFinalStates() {
        return finalStates;
    }

    public void display() {
        System.out.println();
        for (Transition transition : transitions) {
            System.out.println("q" + transition.getFromState() + " {" + FAUtils.join(entries.get(transition.getFromState()), ",")
                    + "} " + transition.getSymbol() + " --> q" + transition.getToState() + " {" + FAUtils.join(entries.get(transition.getToState()), ",")
                    + "}");
        }
        System.out.println("The final states are q : " + FAUtils.join(finalStates, ","));
    }

    public void displayMinDFA() {
        System.out.println();
        for (Transition transition : transitions) {
            var printStr = "q" + transition.getFromState() + " " + transition.getSymbol() + " --> q" + transition.getToState();
            if (finalStates.contains(transition.getToState())) {
                printStr = printStr.concat(" final state ");
            }
            System.out.println(printStr);
        }
    }

    public boolean evaluate(String x) {
        int state = 0;
        for (var i = 0; i < x.length(); i++) {
            char ch = x.charAt(i);
            for (Transition transition : transitions) {
                if (transition.getFromState() == state && transition.getSymbol() == ch) {
                    state = transition.getToState();
                    break;
                }
            }
        }
        return finalStates.contains(state);
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    public int countStates() {
        var stateCount = new HashSet<Integer>();
        for (Transition transition : this.transitions) {
            stateCount.add(transition.getFromState());
            stateCount.add(transition.getToState());
        }
        return stateCount.size();
    }

    public Set<Integer> getAllStatesToBySymbol(int to, char symbol) {
        Set<Integer> result = new HashSet<>();
        for (Transition transition : this.transitions) {
            if (transition.getToState() == to && transition.getSymbol() == symbol) {
                result.add(transition.getFromState());
            }
        }
        return result;
    }

    private Set<Integer> getAllStatesFrom(int from) {
        Set<Integer> result = new HashSet<>();
        for (Transition transition : this.transitions) {
            if (transition.getFromState() == from) {
                result.add(transition.getToState());
            }
        }
        return result;
    }

    private Set<Integer> getStatesFromStartSet(Set<Integer> fromStart) {
        var result = new HashSet<Integer>();
        for (Integer i : fromStart) {
            result.addAll(getAllStatesFrom(i));
        }
        return result;
    }

    public Set<Integer> getReachableStatesFromStart() {
        var start = 0;
        var fromStart = getAllStatesFrom(start);
        Set<Integer> result = new HashSet<>(fromStart);

        Set<Integer> temp = new HashSet<>(result);
        var containAll = false;
        do {
            temp = getStatesFromStartSet(temp);
            containAll = result.containsAll(temp);
            result.addAll(temp);
        } while (!containAll);

        return result;
    }

}
