package ru.bmstu.kibamba.grammar;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<String> productionStr = new ArrayList<>();
        productionStr.add("A -> BC | a");
        productionStr.add("B->CA|Ab|Aa");
        productionStr.add("C -> AB|CC |a");

        var productions = ProductionUtils.createProductionMap(productionStr);

        for(Production p : productions.values()){
            if(ProductionUtils.isProductionContainsLeftRecursion(p)){
                System.out.println(p);
            }
        }

       //var e = ProductionUtils.performsStep02(new Production("B", "CA|BCb|ab"));

        var p04 = ProductionUtils.performsStep04(2,1,productions);

    }
}
