package ru.bmstu.kibamba.grammar;

import java.util.*;
import java.util.stream.Collectors;

public class ProductionUtils {
    private static final List<String> noTerminals = new ArrayList<>();
    private static final String[] potentialsNoTerminals = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
            "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private static Map<Integer, Production> createProductionMap(List<String> productionsStr) {
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

    private static int getProductionNoTerminalIndex(String noTerminal) {
        for (var i = 0; i < noTerminals.size(); i++) {
            if (Objects.equals(noTerminals.get(i), noTerminal)) {
                return i;
            }
        }
        return -1;
    }

    private static boolean isProductionContainsLeftRecursion(Production production) {
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
        return getProductionChainsArray(production.getChain());
    }

    private static String[] getProductionChainsArray(String chain) {
        return chain.split("\\|");
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

    private static String getBetasProductionStr(Set<String> betas) {
        StringBuilder sb = new StringBuilder();
        for (String beta : betas) {
            sb.append(beta).append("|");
        }
        return removeChainLastOrCharacter(sb.toString());
    }

    private static Production getProductionByChain(List<Production> productions, Production production) {
        var productionsToCheck = productions.stream().
                filter(p -> p.getChain().length() == production.getChain().length()).collect(Collectors.toList());
        for (Production pr : productionsToCheck) {
            if (areChainsEquals(pr.getChain(), production.getChain())) {
                return pr;
            }
        }
        return production;
    }

    private static boolean areChainsEquals(String firstChain, String secondChain) {
        var firstChainArray = getProductionChainsArray(firstChain);
        var secondChainArray = getProductionChainsArray(secondChain);
        var count = firstChainArray.length;

        for (String currentChain : firstChainArray) {
            for (String chain : secondChainArray) {
                if (currentChain.equals(chain)) {
                    count--;
                    break;
                }
            }
        }
        return count == 0;
    }

    private static String removeChainLastOrCharacter(String chain) {
        return chain.substring(0, chain.length() - 1);
    }

    private static void performsStep01(Map<Integer, Production> productionMap) {
        var i = 1;
        var n = productionMap.size();
        performsStep02(n, i, productionMap);
    }

    private static void performsStep02(int n, int i, Map<Integer, Production> productionMap) {
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

    private static void performsStep03(int n, int i, Map<Integer, Production> productionMap) {
        if (i == n) {
            return;
        }
        i++;
        var j = 1;
        performsStep04(n, i, j, productionMap);
    }

    private static void performsStep04(int n, int i, int j, Map<Integer, Production> productionsMap) {
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
                Collections.addAll(betas, ajChains);
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

    public static Map<Integer, Production> removeLeftRecursion(List<String> productions) {
        Map<Integer, Production> productionMap = createProductionMap(productions);
        performsStep01(productionMap);

        return productionMap;
    }

    public static List<Production> leftFactorsProduction(Map<Integer, Production> productionWithoutLeftRecursion) {
        List<Production> productions = new ArrayList<>(productionWithoutLeftRecursion.values());
        var index = getProductionToLeftFactoriseIndex(productions);
        while (index != -1) {
            leftFactorsProduction(productions.get(index), productions);
            index = getProductionToLeftFactoriseIndex(productions);
        }

        return productions;
    }

    private static int getProductionToLeftFactoriseIndex(List<Production> productions) {
        for (var i = 0; i < productions.size(); i++) {
            var alpha = findMaxChainFactor(getProductionChainsArray(productions.get(i)));
            if (!alpha.isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private static String findMaxChainFactor(String[] chains) {
        List<String> sortedChains = Arrays.stream(chains).sorted(Comparator.comparingInt(String::length).reversed())
                .collect(Collectors.toList());

        for (String currentChain : sortedChains) {
            var currentChainLength = currentChain.length();
            var chainsToCheck = Arrays.stream(chains).filter(chain -> chain.length() >= currentChainLength)
                    .collect(Collectors.toList());
            var count = 0;
            for (String chain : chainsToCheck) {
                if (chain.startsWith(currentChain)) {
                    count++;
                }
            }
            if (count >= 2) {
                return currentChain;
            }
        }
        return findChainFactor(chains);
    }

    private static String first(String chain) {
        if (chain.length() >= 2) {
            if (chain.substring(0, 2).contains("'")) {
                var i = 2;
                var keepSearching = chain.substring(i).startsWith("'");
                while (keepSearching) {
                    i++;
                    keepSearching = chain.substring(i).startsWith("'");
                }
                return chain.substring(0, i);
            }
        }
        return chain.substring(0, 1);
    }

    private static String findChainFactor(String[] chains) {
        var i = 0;
        var count = 0;
        var first = "";
        do {
            first = first(chains[i]);
            count = count(chains, first);
            i++;
        } while (count <= 1 && i < chains.length);
        return count >= 2 ? first : "";

    }

    private static int count(String[] chains, String first) {
        var count = 0;
        for (String chain : chains) {
            if (chain.startsWith(first)) {
                count++;
            }
        }
        return count;
    }

    private static void leftFactorsProduction(Production production, List<Production> productions) {
        var noTerminal = production.getNoTerminal();
        var chains = getProductionChainsArray(production);
        var alpha = findMaxChainFactor(chains);
        Set<String> betas = new HashSet<>();

        var factor = getFactor(noTerminal);
        var firstAlphaIndex = createBetaListReturnFirstAlphaIndex(chains, alpha, betas);

        Production productionToAdd = new Production(factor, getBetasProductionStr(betas));
        Production productionByChain = getProductionByChain(productions, productionToAdd);
        boolean canAddProduction = factor.equals(productionByChain.getNoTerminal());
        factor = productionByChain.getNoTerminal();

        String modifiedChain = modifyChain(alpha, chains, firstAlphaIndex, factor);
        modifyProduction(modifiedChain, production, canAddProduction, productionToAdd, productions);
    }

    private static String modifyChain(String alpha, String[] chains, int firstAlphaIndex, String factor) {
        StringBuilder modifiedChain = new StringBuilder();
        if (!alpha.isEmpty()) {
            for (var i = 0; i < chains.length; i++) {
                if (i == firstAlphaIndex) {
                    modifiedChain.append(alpha).append(factor).append("|");
                } else {
                    if (!chains[i].startsWith(alpha)) {
                        modifiedChain.append(chains[i]).append("|");
                    }
                }
            }
            if (!noTerminals.contains(factor)) {
                noTerminals.add(factor);
            }
        }

        return modifiedChain.toString();
    }

    private static void modifyProduction(String modifiedChain, Production production, boolean canAddProduction,
                                         Production productionToAdd, List<Production> productions) {

        if (!modifiedChain.isEmpty()) {
            var result = removeChainLastOrCharacter(modifiedChain);
            production.setChain(result);
            if (canAddProduction) {
                productions.add(productionToAdd);
            }
            leftFactorsProduction(production, productions);
        }
    }

    private static int createBetaListReturnFirstAlphaIndex(String[] chains, String alpha, Set<String> betas) {
        var firstAlphaIndex = 0;
        var firstAlphaIndexHasBeenFound = false;
        var count = 0;
        for (String chain : chains) {
            if (chain.startsWith(alpha)) {
                if (!firstAlphaIndexHasBeenFound) {
                    firstAlphaIndexHasBeenFound = true;
                    firstAlphaIndex = count;
                }
                var beta = chain.substring(alpha.length());
                beta = beta.isEmpty() ? "£" : beta;
                betas.add(beta);
            }
            count++;
        }
        return firstAlphaIndex;
    }

    private static String getFactor(String noTerminal) {
        StringBuilder sb = new StringBuilder(noTerminal.contains("'") ? noTerminal : noTerminal.concat("'"));
        if (noTerminals.contains(sb.toString())) {
            var i = 0;
            var j = 0;
            var quotationMark = getQuotationsMark(j);
            var newNoTerminal = new StringBuilder(potentialsNoTerminals[i].concat(quotationMark));
            do {
                i++;
                if (i == potentialsNoTerminals.length) {
                    i = 0;
                    j++;
                    quotationMark = getQuotationsMark(j);
                }
                newNoTerminal = new StringBuilder(potentialsNoTerminals[i].concat(quotationMark));
            } while (noTerminals.contains(newNoTerminal.toString()));

            return newNoTerminal.toString();
        }
        return sb.toString();
    }

    private static String getQuotationsMark(int i) {
        return "'".repeat(Math.max(0, i - 1));
    }
}
