package ru.bmstu.kibamba.files;

import ru.bmstu.kibamba.grammars.Grammar;
import ru.bmstu.kibamba.grammars.Production;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static ru.bmstu.kibamba.grammars.ProductionUtils.getProductionTokenArray;

public class GrammarFileReader {

    public static Grammar readGrammar(String fileName) {
        Set<String> nonterminals = new LinkedHashSet<>();
        Set<String> terminals = new LinkedHashSet<>();
        List<Production> productions = new ArrayList<>();
        String firstSymbol = "";
        try {
            File file = new File(fileName.concat(".txt"));
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
        return new Grammar(nonterminals, terminals, productions, firstSymbol);
    }

    private static Set<String> getSetFromReadLine(String readLine) {
        var characters = getProductionTokenArray(readLine.replaceAll("\\s", ""));
        return new LinkedHashSet<>(characters);
    }

    private static Production getProductionFromReadLine(String readLine) {
        var productions = readLine.replaceAll("\\s", "").split("->");
        return new Production(productions[0], productions[1]);
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
