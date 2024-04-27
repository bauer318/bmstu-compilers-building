package ru.bmstu.kibamba.grammar;

import java.util.*;

public class ProductionUtils {

    private static final List<String> noTerminals = new ArrayList<>();

    public static Map<Integer, Production> createProductionMap(List<String> productionsStr) {
        Map<Integer, Production> result = new HashMap<>();
        var count = 0;
        for (String s : productionsStr) {
            String[] splitProduction = s.replaceAll("\\s", "").split("->");
            var noTerminal = splitProduction[0];
            if (!noTerminals.contains(noTerminal)) {
                noTerminals.add(noTerminal);
            }
            result.put(++count, new Production(noTerminal, splitProduction[1]));
        }
        return result;
    }

    public static int getProductionNoTerminalIndex(String noTerminal) {
        for (var i = 0; i < noTerminals.size(); i++) {
            if (Objects.equals(noTerminals.get(i), noTerminal)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isProductionContainsLeftRecursion(Production production) {
        String productionNoTerminal = production.getNoTerminal();
        int productionNoTerminalIndex = getProductionNoTerminalIndex(productionNoTerminal);

        String[] productionChains = getProductionChainsArray(production);

        for (String chain : productionChains) {
            String currentCh = chain.substring(0, 1);
            if (currentCh.equals(productionNoTerminal)) {
                return true;
            }
            int currentChainFirstIndex = getProductionNoTerminalIndex(currentCh);
            if (currentChainFirstIndex >= 0 && currentChainFirstIndex <= productionNoTerminalIndex) {
                return true;
            }
        }
        return false;
    }

    private static String[] getProductionChainsArray(Production production) {
        return production.getChain().split("\\|");
    }

    private static String getProductionChainStr(List<String> chains, String noTerminal) {
        StringBuilder secondPart = new StringBuilder();
        StringBuilder firstPart = new StringBuilder();
        for (String chain : chains) {
            firstPart.append(chain).append("|");
            secondPart.append(chain).append(noTerminal).append("'").append("|");
        }
        noTerminals.add(noTerminal.concat("'"));
        var sbStr = firstPart.append(secondPart).toString();

        return removeChainLastOrCharacter(sbStr);
    }

    private static String getBetasAlphaProductionChainStr(List<String> betas, String alpha) {
        StringBuilder sb = new StringBuilder();
        for (String beta : betas) {
            sb.append(beta).append(alpha).append("|");
        }
        var sbStr = sb.toString();
        return removeChainLastOrCharacter(sbStr);
    }

    private static String removeChainLastOrCharacter(String chain) {
        return chain.substring(0, chain.length() - 1);
    }

    public static void performsStep01(Map<Integer, Production> productionMap) {
        var i = 1;
        var n = productionMap.size();
        performsStep02(n, i, productionMap);
    }

    public static void performsStep02(int n, int i, Map<Integer, Production> productionMap) {
        var currentProduction = productionMap.get(i);
        if (isProductionContainsLeftRecursion(currentProduction)) {
            String productionNoTerminal = currentProduction.getNoTerminal();

            List<String> alpha = new ArrayList<>();
            List<String> beta = new ArrayList<>();

            String[] productionChains = getProductionChainsArray(currentProduction);

            for (String chain : productionChains) {
                if (chain.substring(0, 1).equals(productionNoTerminal)) {
                    alpha.add(chain.substring(1));
                } else {
                    beta.add(chain);
                }
            }
            var currentProductionModifiedChain = getProductionChainStr(beta, productionNoTerminal);
            currentProduction.setChain(currentProductionModifiedChain);
            var primNoTerminalProd = productionNoTerminal.concat("'");
            var primChain = getProductionChainStr(alpha, productionNoTerminal);
            var primeProd = new Production(primNoTerminalProd, primChain);
            productionMap.put(productionMap.size() + 1, primeProd);
        }

        performsStep03(n, i, productionMap);

    }

    public static void performsStep03(int n, int i, Map<Integer, Production> productionMap) {
        if (i == n) {
            return;
        }
        i++;
        var j = 1;
        performsStep04(n, i, j, productionMap);
    }

    public static void performsStep04(int n, int i, int j, Map<Integer, Production> productionsMap) {
        var ai = productionsMap.get(i);
        var result = new Production(ai.getNoTerminal());
        var aiChains = getProductionChainsArray(ai);

        for (String chain : aiChains) {
            int firstNoTerminalIndex = getProductionNoTerminalIndex(chain.substring(0, 1));
            var resultChain = new StringBuilder();
            if (firstNoTerminalIndex >= 0 && firstNoTerminalIndex + 1 == j) {
                List<String> betas = new ArrayList<>();
                var alpha = chain.substring(1);
                var aj = productionsMap.get(j);
                var ajChains = getProductionChainsArray(aj);
                for(String beta : ajChains){
                    if(!beta.contains("'")){
                        betas.add(beta);
                    }
                }
                resultChain.append(getBetasAlphaProductionChainStr(betas, alpha));
            } else {
                resultChain.append(chain);
            }

            result.setChain(result.getChain()
                    .concat(resultChain.toString())
                    .concat("|"));
        }
        result.setChain(removeChainLastOrCharacter(result.getChain()));
        ai.setChain(result.getChain());

        performsStep05(n, i, j, productionsMap);
    }

    private static void performsStep05(int n, int i, int j, Map<Integer, Production> productionMap) {
        if (j == i - 1) {
            performsStep02(n, i, productionMap);
        } else {
            j++;
            performsStep04(n, i, j, productionMap);
        }
    }

    public static void removeLeftRecursion(List<String> productions) {
        Map<Integer, Production> productionMap = createProductionMap(productions);
        Map<Integer, Production> clonedProductionMap = cloneProductionMap(productionMap);

        performsStep01(productionMap);

        for (Production p : productionMap.values()) {
            System.out.println(p);
        }
    }

    public static Map<Integer, Production> cloneProductionMap(Map<Integer, Production> sourceProductionMap) {
        Map<Integer, Production> result = new HashMap<>();
        sourceProductionMap.forEach((key, value) -> result.put(key, new Production(value.getNoTerminal(), value.getChain())));

        return result;
    }
}
