package ru.bmstu.kibamba.tree;

import java.util.*;

public class RegexRecognizer {
    //a.b
    private static NFA concat(NFA a, NFA b) {
        NFA result = new NFA();
        //No new state added in concatenation
        result.setStates(a.getStatesCount() + b.getStatesCount());

        //Copy all old transitions of a
        for (Transition transition : a.getTransitions()) {
            result.addTransition(transition.getFromState(), transition.getToState(), transition.getSymbol());
        }

        //Creating the link; final state of a will link to initial state of b
        result.addTransition(a.getFinalState(), a.getStatesCount(), 'e');

        //Copy all old transitions of b with offset as a's states have already been added
        var offset = a.getStatesCount();
        for (Transition transition : b.getTransitions()) {
            result.addTransition(transition.getFromState() + offset, transition.getToState() + offset, transition.getSymbol());
        }

        //b is the final state of this created NFA
        result.setFinalState(offset + b.getStatesCount() - 1);
        return result;
    }

    //a*
    private static NFA kleene(NFA a) {
        NFA result = addStateBefore(a);

        var oldFinalState = a.getStatesCount();
        var oldInitialState = 1;
        var newInitialState = 0;
        var newFinalState = oldFinalState + 1;

        //Epsilon transition to new final state
        result.addTransition(oldFinalState, newFinalState, 'e');
        //Reverse epsilon transition
        result.addTransition(oldFinalState, oldInitialState, 'e');
        //Forward total epsilon transition
        result.addTransition(newInitialState, newFinalState, 'e');
        //Mark final state
        result.setFinalState(newFinalState);

        return result;
    }

    //a+
    private static NFA plus(NFA a) {
        NFA result = addStateBefore(a);
        var oldFinalState = a.getStatesCount();
        var oldInitialState = 1;
        var newFinalState = oldFinalState + 1;

        //Epsilon transition to new final state
        result.addTransition(oldFinalState, newFinalState, 'e');
        //Reverse epsilon transition
        result.addTransition(oldFinalState, oldInitialState, 'e');
        //Mark final state
        result.setFinalState(newFinalState);

        return result;
    }

    //s0->s1 as result s0->s1->s2 , where s0->s1 epsilon's transition
    private static NFA addStateBefore(NFA a) {
        NFA result = new NFA();

        /*
         * +2 because we will have one new initial state with epsilon transition to a's initial
         * and one new final state with epsilon transition from a's final state and from the new initial created
         */
        result.setStates(a.getStatesCount() + 2);

        result.addTransition(0, 1, 'e');

        for (Transition transition : a.getTransitions()) {
            result.addTransition(transition.getFromState() + 1, transition.getToState() + 1, transition.getSymbol());
        }

        return result;
    }

    //a|b
    private static NFA orSelection(ArrayList<NFA> selections, int noOfSelections) {
        NFA result = new NFA();
        var stateCount = 2;

        //Find total states by summing all NFAs
        for (var i = 0; i < noOfSelections; i++) {
            stateCount += selections.get(i).getStatesCount();
        }
        result.setStates(stateCount);
        var adderTrack = 1;
        for (var i = 0; i < noOfSelections; i++) {
            //Initial epsilon transition to the first block of 'OR'
            result.addTransition(0, adderTrack, 'e');

            NFA selectedNFA = selections.get(i);
            for (Transition transition : selectedNFA.getTransitions()) {
                result.addTransition(transition.getFromState() + adderTrack, transition.getToState() + adderTrack, transition.getSymbol());
            }
            adderTrack += selectedNFA.getStatesCount();

            //Add epsilon transition to final state
            result.addTransition(adderTrack - 1, stateCount - 1, 'e');
        }
        result.setFinalState(stateCount - 1);
        return result;
    }

    private static boolean isNotOperator(char currentSymbol) {
        return currentSymbol != '(' && currentSymbol != ')' && currentSymbol != '*'
                && currentSymbol != '|' && currentSymbol != '.' && currentSymbol != '+';
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
            if (isNotOperator(currentSymbol)) //Must be a character, so build the simplest NFA
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
                    case '+':
                        NFA plusSym = operands.pop();
                        operands.push(plus(plusSym));
                        break;
                    case '.':
                    case '|':
                    case '(':
                        operators.push(currentSymbol);
                        break;
                    default:
                        operatorCount = 0;
                        operatorSymbol = operators.peek();
                        //Keep searching operands
                        if (operatorSymbol == '(') {
                            continue;
                        }
                        //Collect operands
                        do {
                            operators.pop();
                            operatorCount++;
                        } while (operators.peek() != '(');
                        operators.pop();
                        NFA firstOperand;
                        NFA secondOperand;
                        ArrayList<NFA> selections = new ArrayList<>();
                        if (operatorSymbol == '.') {
                            for (int ii = 0; ii < operatorCount; ii++) {
                                secondOperand = operands.pop();
                                firstOperand = operands.pop();
                                operands.push(concat(firstOperand, secondOperand));
                            }
                        } else if (operatorSymbol == '|') {
                            for (int j = 0; j < operatorCount + 1; j++) {
                                selections.add(new NFA());
                            }

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
        System.out.println("The current recognizer supports characters 'a' and 'b', operations '.', '|', '+' and '*'");
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
