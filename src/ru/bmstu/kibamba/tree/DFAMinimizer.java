package ru.bmstu.kibamba.tree;

import java.util.*;

/**
 * DFAMinimizer minimizes DFA by O(n^2) algorithm with the
 * construction of pairs of distinguishable states
 */
public class DFAMinimizer {

    /**
     * Step 1
     * build a table of lists of inverse edges of size n×|Σ|
     * n - source DFA's states number
     */
    private static Map<FAEdge, Set<Integer>> buildInverseTransition(DFA dfa) {
        var n = dfa.countStates();
        Map<FAEdge, Set<Integer>> dfaInverseEdges = new HashMap<>();

        for (int i = 0; i < n; i++) {
            for (Character c : getLiterate(dfa)) {
                dfaInverseEdges.put(buildFAEdge(i, c), dfa.getAllStatesToBySymbol(i, c));
            }
        }

        return dfaInverseEdges;
    }

    /**
     * Step 2
     * build an array of reachability of states from the starting — reachable of size n
     */
    private static Map<Integer, Boolean> buildReachableStateFromStart(DFA dfa) {
        Map<Integer, Boolean> result = new HashMap<>();
        var reachableStatesFromStart = dfa.getReachableStatesFromStart();
        var dfaStateCount = dfa.countStates();
        for (int i = 0; i < dfaStateCount; i++) {
            result.put(i, reachableStatesFromStart.contains(i));
        }
        return result;
    }

    /**
     * get all terminal states
     */
    private static boolean[] getTerminalStateArray(DFA dfa) {
        var n = dfa.countStates();
        var finalStates = dfa.getFinalStates();
        boolean[] result = new boolean[n];
        for (var i = 0; i < n; i++) {
            result[i] = finalStates.contains(i);
        }
        return result;
    }

    /**
     * Step 3 and 4
     */
    private static boolean[][] buildTable(DFA dfa) {
        int n = dfa.countStates();
        boolean[] isTerminal = getTerminalStateArray(dfa);
        Map<FAEdge, Set<Integer>> dfaInverseEdges = buildInverseTransition(dfa);
        Stack<StatePair> statePairs = new Stack<>();
        Set<Character> literate = getLiterate(dfa);

        boolean[][] marked = new boolean[n][n];

        //Step 3
        for (var i = 0; i < n; i++) {
            for (var j = 0; j < n; j++) {
                if (!marked[i][j] && isTerminal[i] != isTerminal[j]) {
                    marked[i][j] = marked[j][i] = true;
                    statePairs.push(new StatePair(i, j));
                }
            }
        }

        //Step 4
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

    /**
     * Step 6
     * Build the minimized DFA
     */
    private static DFA buildDFA(int[] component, DFA sourceDFA) {
        DFA result = new DFA();
        var oldFinalsState = sourceDFA.getFinalStates();
        var n = sourceDFA.countStates();
        var equivalentStates = getEquivalentState(component, sourceDFA);
        for (var state = 0; state < n; state++) {
            var currentNewState = component[state];
            /*if (areEquivalentState(state, currentNewState, equivalentStates) && currentNewState != state) {
                continue;
            }*/
            for (Transition transition : sourceDFA.getTransitions()) {
                if (transition.getFromState() == state) {
                    var toNewState = component[transition.getToState()];
                    result.setTransition(currentNewState, toNewState, transition.getSymbol());
                }
            }
            if (oldFinalsState.contains(state)) {
                result.setMinDfaFinalState(currentNewState);
            }
        }
        return result;
    }

    private static Map<Integer, List<Integer>> getEquivalentState(int[] component, DFA dfa) {
        var n = dfa.countStates();
        Map<Integer, List<Integer>> result = new HashMap<>();
        for (var i = 0; i < n; i++) {
            var index = new ArrayList<Integer>();
            for (var j = i; j < n; j++) {
                if (component[j] == i) {
                    index.add(j);
                }
            }
            if (index.size() >= 2) {
                result.put(i, index);
            }
        }

        return result;
    }

    private static boolean areEquivalentState(int firstState, int secondState, Map<Integer, List<Integer>> mapEquivalentStates) {
        return (mapEquivalentStates.containsKey(firstState) && mapEquivalentStates.get(firstState).contains(secondState))
                || (mapEquivalentStates.containsKey(secondState) && mapEquivalentStates.get(secondState).contains(firstState));
    }

    private static FAEdge buildFAEdge(int state, char symbol) {
        return new FAEdge(state, symbol);
    }

    private static Set<Character> getLiterate(DFA dfa) {
        ArrayList<Transition> dfaTransitions = dfa.getTransitions();
        Set<Character> literate = new HashSet<>();
        for (Transition transition : dfaTransitions) {
            if (FAUtils.isInputCharacter(transition.getSymbol())) {
                literate.add(transition.getSymbol());
            }
        }

        return literate;
    }


    /**
     * Main's method
     * Minimizes the source DFA
     */
    public static DFA minimization(DFA dfa) {
        boolean[][] marked = buildTable(dfa);
        var reachable = buildReachableStateFromStart(dfa);
        var n = dfa.countStates();
        //Step 5
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

        //Step 6
        return buildDFA(component, dfa);

    }

}
