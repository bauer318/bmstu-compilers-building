import ru.bmstu.kibamba.*;

public class Main {
    public static void main(String[] args) {
        String regex = "(a|b)*abb";
        NFA nfa = RegEx.generateNFA(regex);
        DFA dfa = RegEx.generateDFA(nfa);
        System.out.println(DFASimulator.simulatesDFA("abb",dfa));
    }
}