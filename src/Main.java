import ru.bmstu.kibamba.files.GrammarFileReader;
import ru.bmstu.kibamba.files.GrammarFileWriter;
import ru.bmstu.kibamba.grammars.Grammar;
import ru.bmstu.kibamba.grammars.LanguageNonEmptinessChecker;
import ru.bmstu.kibamba.grammars.LeftRecursionEliminator;
import ru.bmstu.kibamba.grammars.UnreachableCharacterEliminator;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String[] LEFT_RECURSION_TEST_FILENAMES = {
                "input_question1_example2_27",
                "input_question1_example4_7",
                "input_question1_example4_9",
                "input_question1_example4_11"
        };

        String[] USELESS_SYMBOLS_ELIMINATING_TEST_FILENAMES = {
                "input_question2_example_2_22",
                "input_question2_task_2_4_6"
        };
        var firstQuestionFileName = LEFT_RECURSION_TEST_FILENAMES[3];
        var leftRecursionModifiedGrammarFileName = firstQuestionFileName.replace("input", "output");
        var leftFactorizedGrammarFileName = firstQuestionFileName.replace("input", "output_left_fact");

        /*Grammar grammarToEliminateLeftRecursion = GrammarFileReader.readGrammar(firstQuestionFileName);

        Grammar leftRecursionModifiedGrammar = LeftRecursionEliminator.removeLeftRecursion(grammarToEliminateLeftRecursion,
                false);
        GrammarFileWriter.writeGrammar(leftRecursionModifiedGrammar, leftRecursionModifiedGrammarFileName);
        GrammarFileWriter.writeGrammarJsonFile(leftRecursionModifiedGrammar, "G0", leftRecursionModifiedGrammarFileName);

        Grammar leftFactorizedGrammar = LeftRecursionEliminator.leftFactorsProduction(leftRecursionModifiedGrammar);
        GrammarFileWriter.writeGrammar(leftFactorizedGrammar, leftFactorizedGrammarFileName);
        GrammarFileWriter.writeGrammarJsonFile(leftFactorizedGrammar, "G1", leftFactorizedGrammarFileName);*/

        var secondQuestionFileName = USELESS_SYMBOLS_ELIMINATING_TEST_FILENAMES[0];
        var grammarWithOnlyUselessNonterminalsFileName = secondQuestionFileName.replace("input", "output_useless");
        var grammarWithOnlyReachableCharacterFileName = secondQuestionFileName.replace("input", "output_reachable");

        Grammar grammarToEliminateUselessCharacters = GrammarFileReader.readGrammar(secondQuestionFileName);

        Grammar grammarWithOnlyUselessNonterminals = LanguageNonEmptinessChecker.
                eliminatesUnnecessaryNonterminals(grammarToEliminateUselessCharacters);
        GrammarFileWriter.writeGrammar(grammarWithOnlyUselessNonterminals,grammarWithOnlyUselessNonterminalsFileName);
        GrammarFileWriter.writeGrammarJsonFile(grammarWithOnlyUselessNonterminals,"G1",
                grammarWithOnlyUselessNonterminalsFileName);

        Grammar grammarWithOnlyReachableCharacters = UnreachableCharacterEliminator
                .eliminatesUnreachableCharacter(grammarWithOnlyUselessNonterminals);
        GrammarFileWriter.writeGrammar(grammarWithOnlyReachableCharacters,grammarWithOnlyReachableCharacterFileName);
        GrammarFileWriter.writeGrammarJsonFile(grammarWithOnlyReachableCharacters,"G1'",
                grammarWithOnlyReachableCharacterFileName);


    }
}