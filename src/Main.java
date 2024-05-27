import ru.bmstu.kibamba.files.TerminalFileReader;
import ru.bmstu.kibamba.models.GrammarSymbol;
import ru.bmstu.kibamba.parsing.Parser;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<GrammarSymbol> inputExample01 = TerminalFileReader.buildInputChain("example01");

        List<GrammarSymbol> inputExample02 = TerminalFileReader.buildInputChain("example02");

        List<GrammarSymbol> inputWithError = TerminalFileReader.buildInputChain("example_with_error");

        Parser parser = new Parser(inputExample02);
        var isParsed = parser.parse();
        if (isParsed) {
            System.out.println(parser.getRoot());
        }


    }
}