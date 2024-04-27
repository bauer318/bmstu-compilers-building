package ru.bmstu.kibamba.gramatic;

import java.util.*;

public class ProductionUtils {

    private static List<String> noTerminals = new ArrayList<>();

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

    public static char getProductionFirst(Production production) {
        return production.getChain().charAt(0);
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

    private static String getProductionChainStr(List<String> chains) {
        StringBuilder sb = new StringBuilder();
        for (String chain : chains) {
            sb.append(chain).append("|");
        }
        var sbStr = sb.toString();
        return sbStr;
    }

    private static String getProductionChainStr(List<String> chains, String noTerminal) {
        StringBuilder secondPart = new StringBuilder();
        StringBuilder firstPart = new StringBuilder();
        for (String chain : chains) {
            firstPart.append(chain).append("|");
            secondPart.append(chain).append(noTerminal).append("\'").append("|");
        }
        noTerminals.add(noTerminal.concat("\'"));
        var sbStr = firstPart.append(secondPart).toString();

        return sbStr.substring(0, sbStr.length() - 1);
    }

    public static Production[] performsStep02(Production production) {
        Production[] result = new Production[2];
        String productionNoTerminal = production.getNoTerminal();

        List<String> alpha = new ArrayList<>();
        List<String> beta = new ArrayList<>();

        String[] productionChains = getProductionChainsArray(production);

        for (String chain : productionChains) {
            if (chain.substring(0, 1).equals(productionNoTerminal)) {
                alpha.add(chain.substring(1));
            } else {
                beta.add(chain);
            }
        }
        var modifiedProduction = new Production(productionNoTerminal, getProductionChainStr(beta,productionNoTerminal));
        String primNoTerminalProd = productionNoTerminal.concat("\'");
        var primeProd = new Production(primNoTerminalProd,getProductionChainStr(alpha,primNoTerminalProd));
        return result;
    }

    public static Production mergeAllProductionChains(Production production) {
        Production result = new Production(production.getNoTerminal());


        return result;
    }
}
