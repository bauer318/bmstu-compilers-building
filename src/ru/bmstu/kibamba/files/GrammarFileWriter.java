package ru.bmstu.kibamba.files;

import ru.bmstu.kibamba.grammars.Grammar;
import ru.bmstu.kibamba.grammars.Production;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static ru.bmstu.kibamba.grammars.ProductionUtils.getProductionChainsArray;
import static ru.bmstu.kibamba.grammars.ProductionUtils.getProductionTokenArray;

public class GrammarFileWriter {

    public static void writeGrammar(Grammar grammar, String fileName) {
        try {
            FileWriter fileWriter = new FileWriter(fileName.concat(".txt"));
            fileWriter.write(getGrammarSetCharactersSize(grammar.getNonterminals()));
            fileWriter.write(getGrammarCharactersStr(grammar.getNonterminals()));
            fileWriter.write(getGrammarSetCharactersSize(grammar.getTerminals()));
            fileWriter.write(getGrammarCharactersStr(grammar.getTerminals()));
            fileWriter.write(getGrammarProductionsSize(grammar.getProductions()));
            fileWriter.write(getGrammarProductionsStr(grammar.getProductions()));
            fileWriter.write(grammar.getFirstSymbol());
            fileWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String getGrammarSetCharactersSize(Set<String> set) {
        return String.valueOf(set.size()).concat("\n");
    }

    private static String getGrammarProductionsSize(List<Production> productions) {
        return String.valueOf(productions.size()).concat("\n");
    }

    public static String getGrammarCharactersStr(Set<String> set) {
        StringBuilder sb = new StringBuilder();
        for (String v : set) {
            sb.append(v).append(" ");
        }
        return sb.append("\n").toString();
    }

    public static String getGrammarProductionsStr(List<Production> productions) {
        StringBuilder sb = new StringBuilder();
        for (Production production : productions) {
            var chains = getProductionChainsArray(production);
            for (String chain : chains) {
                sb.append(production.getNonterminal()).append(" -> ");
                var tokens = getProductionTokenArray(chain);
                for (String token : tokens) {
                    sb.append(token).append(" ");
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
