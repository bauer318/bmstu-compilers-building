package ru.bmstu.kibamba.grammars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ProductionUtils {
    public static String[] getProductionChainsArray(Production production) {
        return getProductionChainsArray(production.getChain());
    }

    public static String[] getProductionChainsArray(String chain) {
        return chain.split("\\|");
    }

    public static List<String> getProductionTokenArray(String chain) {
        List<String> result = new ArrayList<>();

        for (var i = 0; i < chain.length(); i++) {
            var j = i + 1;
            var incI = 0;

            while (j < chain.length() && chain.charAt(j) == '\'') {
                j++;
                incI++;
            }
            String s;
            if (j >= chain.length()) {
                s = chain.substring(i);
            } else {
                s = chain.substring(i, j);
            }
            result.add(s);
            i += incI;

        }
        return result;
    }

    public static String removeTerminalsFrom(String chain, Set<String> nonterminals) {
        var chainTokens = getProductionTokenArray(chain);
        StringBuilder result = new StringBuilder();
        for (String token : chainTokens) {
            if (nonterminals.contains(token)) {
                result.append(token);
            }
        }
        return result.toString();
    }

}
