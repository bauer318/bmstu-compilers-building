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

        ProductionUtils.removeLeftRecursion(productionStr);
    }
}
