package ru.bmstu.kibamba.grammar;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String> productionStr = new ArrayList<>();
        productionStr.add("A -> BC | a");
        productionStr.add("B->CA|Ab");
        productionStr.add("C -> AB|CC |a");

        /*productionStr.add("E->E+T|T");
        productionStr.add("T->E*F|F");
        productionStr.add("F->(E)|a");*/

        //ProductionUtils.removeLeftRecursion(productionStr);

        String chain = "abCB|abB'CB|aB|a|abCBC'|abB'CBC'|aBC'|aC'";
        String chain1 = "abCB|abB'CBD|aB|a|abCBC'|aBC'|aC'";
        String chain2 = "abCBE|abB'CBD|aB|a|aBC'|aC'";
        String chain3 = "abCBE|abB'CBD|aBF|a|aC'";
        String chain4 = "aG";
        String chain5 = "bcBE|bB'CBD|BF|C'";
        //String chain6 = "acBE|bB'CBD|BF|bC'|a";

        ProductionUtils.setNoTerminals();

        Production pr = new Production("C", chain);
        System.out.println(pr);
        List<Production> prs =  new ArrayList<>();
        ProductionUtils.leftFactorsProduction(pr, prs);

        for(Production p: prs){
            System.out.println(p);
        }
        System.out.println(pr);

        //var e = ProductionUtils.findChainFactor(chain5.split("\\|"));

        //System.out.println(e);

        /*var a = ProductionUtils.findMaxChainFactor(chain5.split("\\|"));
        System.out.println(a);*/


    }
}
