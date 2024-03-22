package ru.bmstu.kibamba.tree;

import java.util.*;

public class DFAMinimizer {

    public static Map<FAEdge, Set<Integer>> buildInverseTransition(DFA dfa) {
        var n = dfa.countStates();
        Map<FAEdge, Set<Integer>> dfaInverseEdges = new HashMap<>();

        for (int i = 0; i < n; i++) {
            for (Character c : getLiterate(dfa)) {
                dfaInverseEdges.put(buildFAEdge(i, c), dfa.getAllStateTo(i, c));
            }
        }

        return dfaInverseEdges;
    }

    public static Map<Integer, Boolean> buildReachableStateFromStart(DFA dfa) {
        Map<Integer, Boolean> result = new HashMap<>();
        var reachableStatesFromStart = dfa.getReachableStatesFromStart();
        var dfaStateCount = dfa.countStates();
        for (int i = 0; i < dfaStateCount; i++) {
            result.put(i, reachableStatesFromStart.contains(i));
        }
        return result;
    }

    public static boolean[] getTerminalStateArray(DFA dfa) {
        var n = dfa.countStates();
        var finalStates = dfa.getFinalStates();
        boolean[] result = new boolean[n];
        for (var i = 0; i < n; i++) {
            result[i] = finalStates.contains(i);
        }
        return result;
    }

    public static boolean[][] buildTable(DFA dfa) {
        int n = dfa.countStates();
        boolean[] isTerminal = getTerminalStateArray(dfa);
        Map<FAEdge, Set<Integer>> dfaInverseEdges = buildInverseTransition(dfa);
        Stack<StatePair> statePairs = new Stack<>();
        Set<Character> literate = getLiterate(dfa);

        boolean[][] marked = new boolean[n][n];


        for (var i = 0; i < n; i++) {
            for (var j = 0; j < n; j++) {
                if (!marked[i][j] && isTerminal[i] != isTerminal[j]) {
                    marked[i][j] = marked[j][i] = true;
                    statePairs.push(new StatePair(i, j));
                }
            }
        }

        while (!statePairs.isEmpty()) {
            var headStatePair = statePairs.pop();
            for (Character c : literate) {
                var rList = dfaInverseEdges.get(buildFAEdge(headStatePair.getI(), c));
                for (Integer r : rList) {
                    var sList = dfaInverseEdges.get(buildFAEdge(headStatePair.getJ(), c));
                    for (Integer s : sList) {
                        if (!marked[r][s]) {
                            marked[r][s] = marked[s][r] = true;
                            statePairs.push(new StatePair(r, s));
                        }
                    }
                }
            }
        }
        return marked;
    }

    public static void minimization(DFA dfa) {
        boolean[][] marked = buildTable(dfa);
        var reachable = buildReachableStateFromStart(dfa);
        var n = dfa.countStates();
        int[] component = new int[n];
        Arrays.fill(component, -1);

        for (var i = 0; i < n; i++) {
            if (!marked[0][i]) {
                component[i] = 0;
            }
        }
        int componentsCount = 0;

        for (var i = 0; i < n; i++) {
            if (!reachable.get(i)) {
                continue;
            }
            if (component[i] == -1) {
                componentsCount++;
                component[i] = componentsCount;
                for (var j = i + 1; j < n; j++) {
                    if (!marked[i][j]) {
                        component[j] = componentsCount;
                    }
                }
            }
        }

    }

    private static FAEdge buildFAEdge(int state, char symbol) {
        return new FAEdge(state, symbol);
    }

    public static Set<Character> getLiterate(DFA dfa) {
        ArrayList<Transition> dfaTransitions = dfa.getTransitions();
        Set<Character> literate = new HashSet<>();
        for (Transition transition : dfaTransitions) {
            if (FAUtils.isInputCharacter(transition.getSymbol())) {
                literate.add(transition.getSymbol());
            }
        }

        return literate;
    }

}
