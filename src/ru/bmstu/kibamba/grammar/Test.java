package ru.bmstu.kibamba.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    }
}
