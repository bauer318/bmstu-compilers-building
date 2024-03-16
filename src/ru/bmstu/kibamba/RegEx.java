package ru.bmstu.kibamba;

import java.util.*;

public class RegEx {
    private static int stateId = 0;

    private static final Stack<NFA> nfaStack = new Stack<>();
    private static final Stack<Character> operator = new Stack<>();

    private static Set<State> set1 = new HashSet<>();
    private static Set<State> set2 = new HashSet<>();

    private static final Set<Character> input = new HashSet<>();

    public static NFA generateNFA(String regular) {
        regular = addConcat(regular);

        input.add('a');
        input.add('b');

        nfaStack.clear();
        operator.clear();

        for (int i = 0; i < regular.length(); i++) {
            if (isInputCharacter(regular.charAt(i))) {
                pushStack(regular.charAt(i));
            } else if (operator.isEmpty()) {
                operator.push(regular.charAt(i));
            } else if (regular.charAt(i) == '(') {
                operator.push(regular.charAt(i));
            } else if (regular.charAt(i) == ')') {
                while (operator.get(operator.size() - 1) != '(') {
                    doOperation();
                }

                operator.pop();
            } else {
                while (!operator.isEmpty() &&
                        priority(regular.charAt(i), operator.get(operator.size() - 1))) {
                    doOperation();
                }
                operator.push(regular.charAt(i));
            }
        }

        while (!operator.isEmpty()) {
            doOperation();
        }

        NFA completedNFA = nfaStack.pop();
        completedNFA.getNfa().get(completedNFA.getNfa().size() - 1).setAcceptState(true);

        return completedNFA;
    }

    private static boolean priority(char first, char second) {
        if (first == second) {
            return true;
        }
        if (first == '*') {
            return false;
        }
        if (second == '*') {
            return true;
        }
        if (first == '.') {
            return false;
        }

        if (second == '.') {
            return true;
        }

        return first != '|';
    }

    private static void doOperation() {
        if (!RegEx.operator.isEmpty()) {
            char charAt = operator.pop();

            switch (charAt) {
                case ('|'):
                    union();
                    break;
                case ('.'):
                    concatenation();
                    break;
                case ('*'):
                    star();
                    break;
                default:
                    System.out.println("Not supported symbol !");
                    System.exit(1);
                    break;
            }
        }
    }

    private static void star() {
        NFA nfa = nfaStack.pop();

        State start = new State(stateId++);
        State end = new State(stateId++);

        start.addTransition(end, 'e');
        start.addTransition(nfa.getNfa().getFirst(), 'e');

        nfa.getNfa().getLast().addTransition(end, 'e');
        nfa.getNfa().getLast().addTransition(nfa.getNfa().getFirst(), 'e');

        nfa.getNfa().addFirst(start);
        nfa.getNfa().addLast(end);

        nfaStack.push(nfa);
    }

    private static void concatenation() {
        NFA nfa2 = nfaStack.pop();
        NFA nfa1 = nfaStack.pop();

        nfa1.getNfa().getLast().addTransition(nfa2.getNfa().getFirst(), 'e');

        for (State s : nfa2.getNfa()) {
            nfa1.getNfa().addLast(s);
        }

        nfaStack.push(nfa1);
    }

    private static void union() {
        NFA nfa2 = nfaStack.pop();
        NFA nfa1 = nfaStack.pop();

        State start = new State(stateId++);
        State end = new State(stateId++);

        start.addTransition(nfa1.getNfa().getFirst(), 'e');
        start.addTransition(nfa2.getNfa().getFirst(), 'e');

        nfa1.getNfa().getLast().addTransition(end, 'e');
        nfa2.getNfa().getLast().addTransition(end, 'e');

        nfa1.getNfa().addFirst(start);
        nfa2.getNfa().addLast(end);

        for (State s : nfa2.getNfa()) {
            nfa1.getNfa().addLast(s);
        }
        nfaStack.push(nfa1);
    }

    private static void pushStack(char symbol) {
        State s0 = new State(stateId++);
        State s1 = new State(stateId++);

        s0.addTransition(s1, symbol);

        NFA nfa = new NFA();

        nfa.getNfa().addLast(s0);
        nfa.getNfa().addLast(s1);

        nfaStack.push(nfa);
    }

    private static String addConcat(String regular) {
        StringBuilder newRegular = new StringBuilder();

        for (int i = 0; i < regular.length() - 1; i++) {
            if (isInputCharacter(regular.charAt(i)) &&
                    isInputCharacter(regular.charAt(i + 1))) {
                newRegular.append(regular.charAt(i)).append(".");
            } else if (isInputCharacter(regular.charAt(i)) && regular.charAt(i + 1) == '(') {
                newRegular.append(regular.charAt(i)).append(".");
            } else if (regular.charAt(i) == ')' && isInputCharacter(regular.charAt(i + 1))) {
                newRegular.append(regular.charAt(i)).append(".");
            } else if (regular.charAt(i) == '*' && isInputCharacter(regular.charAt(i + 1))) {
                newRegular.append(regular.charAt(i)).append(".");
            } else if (regular.charAt(i) == '*' && regular.charAt(i + 1) == '(') {
                newRegular.append(regular.charAt(i)).append(".");
            } else if (regular.charAt(i) == ')' && regular.charAt(i + 1) == '(') {
                newRegular.append(regular.charAt(i)).append(".");
            } else {
                newRegular.append(regular.charAt(i));
            }
        }
        newRegular.append(regular.charAt(regular.length() - 1));
        return newRegular.toString();
    }

    private static boolean isInputCharacter(char charAt) {
        return charAt == 'a' || charAt == 'b' || charAt == 'e';
    }

    public static DFA generateDFA(NFA nfa) {
        DFA dfa = new DFA();
        stateId = 0;

        LinkedList<State> unprocessed = new LinkedList<>();

        set1 = new HashSet<>();
        set2 = new HashSet<>();

        set1.add(nfa.getNfa().getFirst());
        removeEpsilonTransition();

        State dfaStart = new State(stateId++, set2);

        dfa.getDfa().addLast(dfaStart);
        unprocessed.addLast(dfaStart);

        while (!unprocessed.isEmpty()) {
            State state = unprocessed.removeLast();

            for (Character symbol : input) {
                set1 = new HashSet<>();
                set2 = new HashSet<>();

                moveStates(symbol, state.getStates(), set1);
                removeEpsilonTransition();

                boolean found = false;
                State st = null;

                for (int i = 0; i < dfa.getDfa().size(); i++) {
                    st = dfa.getDfa().get(i);

                    if (st.getStates().containsAll(set2)) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    State p = new State(stateId++, set2);
                    unprocessed.addLast(p);
                    dfa.getDfa().addLast(p);
                    state.addTransition(p, symbol);
                } else {
                    state.addTransition(st, symbol);
                }
            }
        }

        return dfa;
    }

    private static void removeEpsilonTransition() {
        Stack<State> stateStack = new Stack<>();
        set2 = set1;

        for (State s : set1) {
            stateStack.push(s);
        }

        while (!stateStack.isEmpty()) {
            State st = stateStack.pop();

            ArrayList<State> epsilonStates = st.getAllTransitions('e');

            for (State s : epsilonStates) {
                if (!set2.contains(s)) {
                    set2.add(s);
                    stateStack.push(s);
                }
            }
        }
    }

    private static void moveStates(Character c, Set<State> states, Set<State> set) {

        ArrayList<State> temp = new ArrayList<>(states);
        for (State st : temp) {
            ArrayList<State> allStates = st.getAllTransitions(c);

            set.addAll(allStates);
        }
    }
}
