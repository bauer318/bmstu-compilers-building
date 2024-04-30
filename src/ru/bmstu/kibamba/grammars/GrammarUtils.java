package ru.bmstu.kibamba.grammars;

import ru.bmstu.kibamba.dto.*;
import ru.bmstu.kibamba.dto.Grammar;

import java.util.*;

public class GrammarUtils {
    public static String[] getProductionChainsArray(Production production) {
        return getProductionChainsArray(production.getChain());
    }

    public static String getFileNameWithExtension(String fileName) {
        return fileName.endsWith(".txt") ? fileName : fileName.concat(".txt");
    }

    public static List<Production> getAllProductionForNonterminal(String nonterminal, List<Production> productions) {
        List<Production> result = new ArrayList<>();

        for (Production production : cloneProductions(productions)) {
            if (production.getNonterminal().equals(nonterminal)) {
                result.add(production);
                productions.remove(production);
            }
        }
        return result;
    }

    public static List<String> getProductionsStr(List<Production> productions) {
        List<String> result = new ArrayList<>();
        for (Production production : productions) {
            result.add(production.getNonterminal().concat("->").concat(production.getChain()));
        }
        return result;
    }

    public static List<Production> buildProduction(Map<Integer, Production> productionMap) {
        return new ArrayList<>(productionMap.values());
    }

    public static Set<String> getNonterminals(List<Production> productions) {
        Set<String> result = new LinkedHashSet<>();
        for (Production production : productions) {
            result.add(production.getNonterminal());
        }
        return result;
    }

    public static Set<String> getTerminals(List<Production> productions) {
        Set<String> result = new LinkedHashSet<>();
        for (Production production : productions) {
            var chains = getProductionChainsArray(production);
            for (String chain : chains) {
                var tokens = getProductionTokenArray(chain);
                for (String token : tokens) {
                    if (token.equals(token.toLowerCase())) {
                        result.add(token);
                    }
                }
            }
        }
        return result;
    }

    public static String removeChainLastOrCharacter(String chain) {
        return chain.substring(0, chain.length() - 1);
    }

    public static List<Production> cloneProductions(List<Production> productions) {
        List<Production> clonedProductions = new ArrayList<>();
        for (Production production : productions) {
            clonedProductions.add(production.clone());
        }
        return clonedProductions;
    }

    public static List<Production> eliminateEpsilonFactor(List<Production> productions) {
        List<Production> result = new ArrayList<>();
        for (Production production : productions) {
            var modifiedProd = new Production(production.getNonterminal());
            StringBuilder modifiedChain = new StringBuilder();
            var chains = getProductionChainsArray(production);
            for (String chain : chains) {
                if (chain.contains("£") && chain.length() >= 2) {
                    chain = chain.replace("£", "");
                }
                modifiedChain.append(chain).append("|");
            }
            modifiedProd.setChain(removeChainLastOrCharacter(modifiedChain.toString()));
            result.add(modifiedProd);
        }
        return result;
    }

    public static String[] getProductionChainsArray(String chain) {
        return chain.split("\\|");
    }

    public static List<String> getProductionTokenArray(String chain) {
        List<String> result = new ArrayList<>();
        var length = chain.length();
        chain = chain.replace("id", "");
        if (length > chain.length()) {
            result.add("id");
        }
        length = chain.length();
        chain = chain.replace("num", "");
        if (length > chain.length()) {
            result.add("num");
        }

        for (var i = 0; i < chain.length(); i++) {
            var j = i + 1;
            var incI = 0;

            while (j < chain.length() && (chain.charAt(j) == '\'')) {
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

    public static boolean isAlphaBelongSet(String alpha, Set<String> terminalsNonterminals) {
        var alphaTokens = getProductionTokenArray(alpha);
        for (String token : alphaTokens) {
            if (!terminalsNonterminals.contains(token)) {
                return false;
            }
        }
        return true;
    }

    public static Grammar buildGrammarDTO(ru.bmstu.kibamba.grammars.Grammar grammar, String name) {
        List<Term> terminalSymbols = getTerminalSymbols(grammar.getTerminals());
        List<Nonterm> nonterminalSymbols = getNonterminalSymbols(grammar.getNonterminals());
        List<ru.bmstu.kibamba.dto.Production> productions = getProductions(grammar.getProductions());
        StartSymbol startSymbol = new StartSymbol(grammar.getFirstSymbol());

        return new Grammar(name, terminalSymbols, nonterminalSymbols, productions, startSymbol);
    }

    public static List<Term> getTerminalSymbols(Set<String> terminals) {
        List<Term> result = new ArrayList<>();
        for (String terminal : terminals) {
            result.add(new Term(terminal, terminal));
        }
        return result;
    }

    public static List<Nonterm> getNonterminalSymbols(Set<String> nonterminals) {
        List<Nonterm> result = new ArrayList<>();
        for (String nonterm : nonterminals) {
            result.add(new Nonterm(nonterm));
        }
        return result;
    }

    public static List<ru.bmstu.kibamba.dto.Production> getProductions(List<Production> productions) {
        List<ru.bmstu.kibamba.dto.Production> result = new ArrayList<>();
        for (Production production : productions) {
            var chains = getProductionChainsArray(production);
            List<Symbol> symbols = new ArrayList<>();
            var lhs = new Lhs(production.getNonterminal());
            for (String chain : chains) {
                var tokens = getProductionTokenArray(chain);
                for (String token : tokens) {
                    symbols.add(new Symbol(token, token));
                }
            }
            var rhs = new Rhs(symbols);
            result.add(new ru.bmstu.kibamba.dto.Production(lhs, rhs));
        }
        return result;
    }


}
