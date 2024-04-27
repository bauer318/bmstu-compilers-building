package ru.bmstu.kibamba.grammar;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String> productionStr = new ArrayList<>();
        /*productionStr.add("A -> BC | a");
        productionStr.add("B->CA|Ab");
        productionStr.add("C -> AB|CC |a");*/

        productionStr.add("E->E+T|T");
        productionStr.add("T->E*F|F");
        productionStr.add("F->(E)|a");

        //var productions = ProductionUtils.createProductionMap(productionStr);

        /*for(Production p : productions.values()){
            if(ProductionUtils.isProductionContainsLeftRecursion(p)){
                System.out.println(p);
            }
        }*/

       //var e = ProductionUtils.performsStep02(new Production("B", "CA|BCb|ab"));

        //var p04 = ProductionUtils.performsStep04(2,1,productions);

        ProductionUtils.removeLeftRecursion(productionStr);

        /*var a = ProductionUtils.createProductionMap(productionStr);
        var b = ProductionUtils.cloneProductionMap(a);

        var c = b.get(1);
        c.setChain("AD");

        for(Production p : a.values()){
            System.out.println(p);
        }*/

    }
}
