package ru.bmstu.kibamba.tree;

import java.util.ArrayList;

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
            for (int vertex : entry) {
                if (vertex == nfaFinalState) {
                    finalStates.add(i);
                }
            }
        }
    }

    String getFinalState() {
        return FAUtils.join(finalStates, ",");
    }

    void setTransition(int vertex_from, int vertex_to, char symbol) {
        Transition new_trans = new Transition(vertex_from, vertex_to, symbol);
        transitions.add(new_trans);
    }

    void display() {
        Transition newTransition;
        System.out.println();
        for (Transition transition : transitions) {
            newTransition = transition;
            System.out.println("q" + newTransition.getFromState() + " {" + FAUtils.join(entries.get(newTransition.getFromState()), ",")
                    + "} -> q" + newTransition.getToState() + " {" + FAUtils.join(entries.get(newTransition.getToState()), ",")
                    + "} : Symbol - " + newTransition.getSymbol());
        }
        System.out.println("The final state is q : " + FAUtils.join(finalStates, ","));
    }

    void showTransitions() {
        for (Transition transition : transitions) {
            System.out.println(transition.getFromState() + "," + transition.getToState() + "," + transition.getSymbol());
        }
    }

    void showEntries() {
        for (ArrayList<Integer> entry : entries) {
            for (Integer integer : entry) System.out.print(integer + ",");
            System.out.println();
        }
    }

    void showFinal() {
        for (Integer finalState : finalStates) {
            System.out.println(finalState);
        }
    }

    boolean evaluate(String x)//Evaluates the string
    {
        int i, l = x.length(), j;
        int state = 0;
        for (i = 0; i < l; i++) {
            char ch = x.charAt(i);
            for (j = 0; j < transitions.size(); j++) {
                Transition t = transitions.get(j);
                if (t.getFromState() == state && t.getSymbol() == ch) {
                    state = t.getToState();
                    break;
                }
            }
            if (j == transitions.size())
                return false;
        }
        return finalStates.contains(state);
    }
}
