package ru.bmstu.kibamba.grammars;

import ru.bmstu.kibamba.files.GrammarFileReader;
import ru.bmstu.kibamba.files.GrammarFileWriter;

import java.util.*;

public class Test {
    public static void main(String[] args) {
        List<String> productionStr = new ArrayList<>();
        /*productionStr.add("A -> BC | a");
        productionStr.add("B->CA|Ab");
        productionStr.add("C -> AB|CC |a");*/
        productionStr.add("A->aAB|aBc|aAc");

        /*productionStr.add("E->E+T|T");
        productionStr.add("T->E*F|F");
        productionStr.add("F->(E)|a");*/

        /*var a = ProductionUtils.removeLeftRecursion(productionStr);
        ProductionUtils.printNoTerminals();
        for (Production production : a.values()) {
            System.out.println(production);
        }
        System.out.println("--------------------------------");
        var b = ProductionUtils.leftFactorsProduction(a);
        for (Production production : b) {
            System.out.println(production);
        }*/

        /*Map<Integer, String> str = new HashMap<>();
        str.put(1,"text1");
        str.put(2,"text2");*/

        /*for(String s : str.values()){
            s = "modied".concat(s);
            //System.out.println(s);
        }*/

        /*for(String s : str.values()){
            if(s.endsWith("1")){
                str.put(3,s.concat("e"));
            }
            System.out.println(s);
        }*/

       /*String chain = "abCB|abB'CB|aB|a|abCBC'|abB'CBC'|aBC'|aC'";
        String chain1 = "abCB|abB'CBD|aB|a|abCBC'|aBC'|aC'";
        String chain2 = "abCBE|abB'CBD|aB|a|aBC'|aC'";
        String chain3 = "abCBE|abB'CBD|aBF|a|aC'";
        String chain4 = "aG";
        String chain5 = "bcBE|bB'CBD|BF|C'";*/
        //String chain6 = "acBE|bB'CBD|BF|bC'|a";

        /*ProductionUtils.setNoTerminals();

        Production pr = new Production("C", chain);
        System.out.println(pr);
        List<Production> prs =  new ArrayList<>();
        ProductionUtils.leftFactorsProduction(pr, prs);

        for(Production p: prs){
            System.out.println(p);
        }
        System.out.println(pr);*/

        //var e = ProductionUtils.findChainFactor(chain5.split("\\|"));

        //System.out.println(e);

        /*var a = ProductionUtils.findMaxChainFactor(chain5.split("\\|"));
        System.out.println(a);*/

        Set<String> n1 = new LinkedHashSet<>();
        n1.add("S");
        n1.add("A");
        n1.add("B");

        Set<String> t1 = new LinkedHashSet<>();
        t1.add("a");
        t1.add("b");
        List<Production> p1 = List.of(new Production("S", "a|A"),
                new Production("A", "AB"),
                new Production("B", "b"));
        List<Production> p2 = List.of(new Production("O", "BC|a"), new Production("F", "B|C"));
        String s1 = "S";


        Grammar g = new Grammar(n1, t1, p1, s1);

        //GrammarFileWriter.writeGrammar(g, "input");
        Grammar g2 = GrammarFileReader.readGrammar("input");
        System.out.println(g2);

        /*Grammar g1 = LanguageNonEmptinessChecker.eliminatesUnnecessaryNonterminals(g);

        System.out.println(g);
        System.out.println(g1);

        n1 = new LinkedHashSet<>();
        n1.add("E");
        n1.add("T");
        n1.add("F");

        t1 = new LinkedHashSet<>();
        t1.add("(");
        t1.add(")");
        t1.add("a");
        t1.add("+");
        t1.add("*");
        p1 = List.of(new Production("E", "E+T|T"),

                new Production("F", "(E)|a"));
        s1 = "E";

        g = new Grammar(n1, t1, p1, s1);

        var g4 = UnreachableCharacterEliminator.eliminatesUnreachableCharacter(g1);
        //Grammar g2 = LanguageNonEmptinessChecker.eliminatesUnnecessaryNonterminals(g1);
        System.out.println(g4);

        var a = LanguageNonEmptinessChecker.performStep01(g1);
        for (String st : a) {
            System.out.println(st);
        }*/

        /*var a = ProductionUtils.getProductionTokenArray("a'''B'cbA''a");
        for (String st : a) {
            System.out.println(st);
        }*/
        //Grammar g2 = g1.clone();
        /*g2.setNonterminals(n2);
        g2.setProductions(p2);*/
        /*System.out.println("N1");
        for(String st : g1.getNonterminals()){
            System.out.print(st+" ");
        }*/
        /*System.out.println("N2");
        for(String st : g1.getNonterminals()){
            System.out.println(st);
        }*/
        /*System.out.println("\nT1");
        for(String st : g1.getTerminals()){
            System.out.println(st);
        }

        System.out.println("P1");
        for(Production st : g1.getProductions()){
            System.out.println(st);
        }*/
        /*System.out.println("P2");
        for(Production st : g2.getProductions()){
            System.out.println(st);
        }*/

    }
}
