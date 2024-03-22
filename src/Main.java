import ru.bmstu.kibamba.tree.DFA;
import ru.bmstu.kibamba.tree.DFAMinimizer;
import ru.bmstu.kibamba.tree.NFA;
import ru.bmstu.kibamba.tree.RegexRecognizer;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        //RegexRecognizer.recognise();
        NFA nfa = RegexRecognizer.regexToNfa("(a|b)*abb");
        DFA dfa = RegexRecognizer.nfaToDfa(nfa);
        DFAMinimizer.minimization(dfa);

        //System.out.println(dfa.countStates());
        //System.out.println(s);

    }
}