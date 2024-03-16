package ru.bmstu.kibamba;

import java.util.ArrayList;
import java.util.LinkedList;

public class DFASimulator {
    private static int i = 0;
    private static String src = "";

    public static String simulatesDFA(String x, DFA dfa) {
        src = x.concat("E");
        State currentState = DFAUtils.getInitialState(dfa);

        State lastState = DFAUtils.getLastState(dfa);
        ArrayList<TransitionFunction> transitionFunctions = DFAUtils.generateTransitionFunctionFromDfa(dfa);

        char c = nextChar();
        while (c != 'E') {
            currentState = DFAUtils.move(currentState, c, transitionFunctions);
            c = nextChar();
        }


        assert currentState != null;
        return currentState.isAcceptState() ? "Да" : "Нет";
    }

    private static char nextChar() {
        return src.charAt(i++);
    }

}
