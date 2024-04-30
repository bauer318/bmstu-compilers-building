package ru.bmstu.kibamba.files;

import ru.bmstu.kibamba.grammars.Grammar;
import ru.bmstu.kibamba.grammars.Production;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static ru.bmstu.kibamba.grammars.GrammarUtils.*;

public class GrammarFileReader {

    public static Grammar readGrammar(String fileName) {
        Set<String> nonterminals = new LinkedHashSet<>();
        Set<String> terminals = new LinkedHashSet<>();
        List<Production> productions = new ArrayList<>();
        String firstSymbol = "";
        try {
            File file = new File(getFileNameWithExtension(fileName));
            int lineNumber = getFileLinesNumber(file);
            Scanner sc = new Scanner(file);
            var count = 0;

            while (sc.hasNextLine()) {
                ++count;
                var line = sc.nextLine();
                if (count == 2) {
                    nonterminals = getSetFromReadLine(line);
                } else if (count == 4) {
                    terminals = getSetFromReadLine(line);
                } else if (count >= 6 && count < lineNumber) {
                    productions.add(getProductionFromReadLine(line));
                } else if (count == lineNumber) {
                    firstSymbol = line;
                }
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        productions = mergeProductionChains(productions);
        return new Grammar(nonterminals, terminals, productions, firstSymbol);
    }

    private static Set<String> getSetFromReadLine(String readLine) {
        var characters = getProductionTokenArray(readLine.
                replaceAll("\\s", ""));
        return new LinkedHashSet<>(characters);
    }

    private static Production getProductionFromReadLine(String readLine) {
        var productions = readLine.replaceAll("\\s", "").split("->");
        return new Production(productions[0], productions[1]);
    }

    private static List<Production> mergeProductionChains(List<Production> productions) {
        List<Production> result = new ArrayList<>();
        Set<String> nonterminals = new LinkedHashSet<>();

        for (Production production : productions) {
            nonterminals.add(production.getNonterminal());
        }

        for (String nonterminal : nonterminals) {
            StringBuilder mergedChains = new StringBuilder();
            List<Production> currentProductions = getAllProductionForNonterminal(nonterminal, productions);
            for (Production production : currentProductions) {
                mergedChains.append(production.getChain()).append("|");
            }
            var chain = removeChainLastOrCharacter(mergedChains.toString());
            result.add(new Production(nonterminal, chain));
        }
        return result;
    }

    private static int getFileLinesNumber(File file) {
        var count = 0;
        Scanner sc = null;
        try {
            sc = new Scanner(file);
            while (sc.hasNextLine()) {
                sc.nextLine();
                count++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            assert sc != null;
            sc.close();
        }
        return count;

    }
}
