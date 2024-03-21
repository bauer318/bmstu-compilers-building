package ru.bmstu.kibamba.tree;

import java.util.*;

public class RegexRecognizer {
    private static NFA concat(NFA a, NFA b) {
        NFA result = new NFA();
        result.setStates(a.getStatesCount() + b.getStatesCount());//No new vertex added in concatenation
        int i;
        Transition newTransition;
        for (i = 0; i < a.getTransitions().size(); i++) {
            newTransition = a.getTransitions().get(i);
            result.addTransition(newTransition.getFromState(), newTransition.getToState(), newTransition.getSymbol());//Copy old transitions
        }
        result.addTransition(a.getFinalState(), a.getStatesCount(), '^');//Creating the link; final state of a will link to initial state of b
        for (i = 0; i < b.getTransitions().size(); i++) {
            newTransition = b.getTransitions().get(i);
            result.addTransition(newTransition.getFromState() + a.getStatesCount(), newTransition.getToState() + a.getStatesCount(), newTransition.getSymbol());//Copy old transitions wit offset as a's vertices have already been added
        }
        result.setFinalState(a.getStatesCount() + b.getStatesCount() - 1);//Mark b's final as final in new one too
        return result;
    }

    private static NFA kleene(NFA a) {
        NFA result = new NFA();
        int i;
        Transition newTransition;
        result.setStates(a.getStatesCount() + 2);
        result.addTransition(0, 1, '^');//Epsilon transition from S0 to S1
        for (i = 0; i < a.getTransitions().size(); i++) {
            newTransition = a.getTransitions().get(i);
            result.addTransition(newTransition.getFromState() + 1, newTransition.getToState() + 1, newTransition.getSymbol());//Copy old transitions
        }
        result.addTransition(a.getStatesCount(), a.getStatesCount() + 1, '^');//Epsilon transition to new final state
        result.addTransition(a.getStatesCount(), 1, '^');//Reverse epsilon transition
        result.addTransition(0, a.getStatesCount() + 1, '^');//Forward total epsilon transition
        result.setFinalState(a.getStatesCount() + 1);//Mark final state
        return result;
    }

    private static NFA orSelection(ArrayList<NFA> selections, int noOfSelections) {
        NFA result = new NFA();
        int vertexCount = 2;
        int i, j;
        NFA med;
        Transition newTransition;
        for (i = 0; i < noOfSelections; i++) {
            vertexCount += selections.get(i).getStatesCount();//Find total vertices by summing all NFAs
        }
        result.setStates(vertexCount);
        int adderTrack = 1;
        for (i = 0; i < noOfSelections; i++) {
            result.addTransition(0, adderTrack, '^');//Initial epsilon transition to the first block of 'OR'
            med = selections.get(i);
            for (j = 0; j < med.getTransitions().size(); j++) {
                newTransition = med.getTransitions().get(j);
                result.addTransition(newTransition.getFromState() + adderTrack, newTransition.getToState() + adderTrack, newTransition.getSymbol());//Copy all transitions in first NFA
            }
            adderTrack += med.getStatesCount();//Find how many vertices added
            result.addTransition(adderTrack - 1, vertexCount - 1, '^');//Add epsilon transition to final state
        }
        result.setFinalState(vertexCount - 1);//Mark final state
        return result;
    }

    private static boolean isNotOperator(char currentSymbol) {
        return currentSymbol != '(' && currentSymbol != ')' && currentSymbol != '*' && currentSymbol != '|' && currentSymbol != '.';
    }

    private static NFA regexToNfa(String regex) {
        regex = FAUtils.normalizeInputRegex(regex);
        Stack<Character> operators = new Stack<>();
        Stack<NFA> operands = new Stack<>();
        char operatorSymbol;
        int operatorCount;
        char currentSymbol;
        NFA newSym;
        char[] x = regex.toCharArray();
        for (char value : x) {
            currentSymbol = value;
            if (isNotOperator(currentSymbol)) //Must be a character, so build simplest NFA
            {
                newSym = new NFA();
                newSym.setStates(2);
                newSym.addTransition(0, 1, currentSymbol);
                newSym.setFinalState(1);
                operands.push(newSym);//push it back
            } else {
                switch (currentSymbol) {
                    case '*':
                        NFA starSym = operands.pop();
                        operands.push(kleene(starSym));
                        break;
                    case '.':
                    case '|':
                    case '(':
                        operators.push(currentSymbol);
                        break;
                    default:
                        operatorCount = 0;
                        operatorSymbol = operators.peek();//See which symbol is on top
                        if (operatorSymbol == '(')
                            continue;//Keep searching operands
                        do {
                            operators.pop();
                            operatorCount++;
                        } while (operators.peek() != '(');//Collect operands
                        operators.pop();
                        NFA firstOperand;
                        NFA secondOperand;
                        ArrayList<NFA> selections = new ArrayList<>();
                        if (operatorSymbol == '.') {
                            for (int ii = 0; ii < operatorCount; ii++) {
                                secondOperand = operands.pop();
                                firstOperand = operands.pop();
                                operands.push(concat(firstOperand, secondOperand));//Concatenate and add back
                            }
                        } else if (operatorSymbol == '|') {
                            for (int j = 0; j < operatorCount + 1; j++)
                                selections.add(new NFA());
                            int tracker = operatorCount;
                            for (int k = 0; k < operatorCount + 1; k++) {
                                selections.set(tracker, operands.pop());
                                tracker--;
                            }
                            operands.push(orSelection(selections, operatorCount + 1));
                        }
                        break;
                }
            }
        }
        return operands.peek();//Return the single entity. operands.poll() is also fine
    }

    private static DFA nfaToDfa(NFA nfa) {
        DFA dfa = new DFA();
        ArrayList<Integer> start = new ArrayList<>();
        start.add(0);
        ArrayList<Integer> s0 = nfa.eclosure(start);
        int stateFrom = dfa.addEntry(s0);
        while (stateFrom != -1) {
            ArrayList<Integer> T = dfa.entryAt(stateFrom);
            dfa.markEntry(stateFrom);
            ArrayList<Character> symbols = nfa.findPossibleInputSymbols(T);
            for (char a : symbols) {
                ArrayList<Integer> U = nfa.eclosure(nfa.move(T, a));
                int stateTo = dfa.findEntry(U);
                if (stateTo == -1) { // U not already in S'
                    stateTo = dfa.addEntry(U);
                }
                dfa.setTransition(stateFrom, stateTo, a);
            }
            stateFrom = dfa.nextUnmarkedEntryIdx();
        }
        // The finish states of the DFA are those which contain any
        // of the finish states of the NFA.
        dfa.setFinalState(nfa.getFinalState());
        return dfa;
    }

    public static void recognise() {
        System.out.println("\nThe Thompson's Construction Algorithm takes a regular expression as " +
                "an input and returns its corresponding Non-Deterministic Finite Automaton \n");
        System.out.println("The current recognizer supports characters 'a' and 'b', operations '.', '|' and '*'");
        System.out.println("Metadata '(' and ')' \n\n");
        System.out.println("Enter the regular expression. Ex: (a|b)*aab");
        String regex = new Scanner(System.in).next();
        System.out.println("\nThe required NFA has the transitions: ");
        NFA requiredNfa;
        requiredNfa = regexToNfa(regex);
        requiredNfa.display();
        System.out.println("\nDFA :");
        DFA requiredDfa = nfaToDfa(requiredNfa);
        requiredDfa.display();
        String eval;
        do {
            System.out.println("Enter string to evaluate " + regex + " or tap 0 to stop");
            eval = new Scanner(System.in).next();
            if (!eval.equals("0")) {
                if (requiredDfa.evaluate(eval)) {
                    System.out.println(eval + " is accepted by regex " + regex);
                } else {
                    System.out.println(eval + " is rejected by regex " + regex);
                }
            }
        } while (!eval.equals("0"));
    }
}
