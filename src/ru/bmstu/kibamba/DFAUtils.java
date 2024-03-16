package ru.bmstu.kibamba;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

public class DFAUtils {

    public static ArrayList<TransitionFunction> generateTransitionFunctionFromDfa(DFA dfa) {
        ArrayList<TransitionFunction> transitionFunction = new ArrayList<>();
        LinkedList<State> states = dfa.getDfa();
        ArrayList<String> acceptNos = new ArrayList<>();
        for (State state : states) {
            if (state.isAcceptState()) {
                acceptNos.add(state.getId() + "");
            }
        }

        for (State state : states) {
            StringBuilder stateName = new StringBuilder(state.getId() + "");
            for (String acceptedState : acceptNos) {
                if (stateName.toString().equals(acceptedState)) {
                    stateName.insert(0, "accept");
                    break;
                }
            }

            for (Map.Entry<Character, ArrayList<State>> entry : state.getNextState().entrySet()) {
                char ch = entry.getKey();
                State toState = entry.getValue().get(0);
                String id = toState.getId() + "";
                for (String acceptedState : acceptNos) {
                    if (id.equals(acceptedState)) {
                        id = "accept" + id;
                        break;
                    }
                }
                TransitionFunction tr = new TransitionFunction(state, toState, ch);
                transitionFunction.add(tr);
            }
        }
        return transitionFunction;
    }

    public static State getInitialState(DFA dfa) {
        LinkedList<State> states = dfa.getDfa();
        for (State state : states) {
            if (state.getId() == 0) {
                return state;
            }
        }
        return null;
    }

    public static State getLastState(DFA dfa) {
        LinkedList<State> states = dfa.getDfa();
        for (State state : states) {
            if (state.isAcceptState()) {
                return state;
            }
        }
        return null;
    }

    public static State move(State fromState, char c, ArrayList<TransitionFunction> transitionFunctions) {
        for (TransitionFunction transitionFunction : transitionFunctions) {
            if (transitionFunction.getFrom().getId() == fromState.getId() && transitionFunction.getWith() == c) {
                return transitionFunction.getTo();
            }
        }
        return null;
    }
}
