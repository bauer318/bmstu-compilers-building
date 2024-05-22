package ru.bmstu.kibamba.files;

import ru.bmstu.kibamba.models.GrammarSymbol;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static ru.bmstu.kibamba.parsing.TerminalBuilder.*;

public class TerminalFileReader {

    private static String[] ids = {"a, b, c, d, e, f,g,d,h,i,j,k"};


    public static List<GrammarSymbol> buildInputChain(String fileName) {
        List<GrammarSymbol> result = new LinkedList<>();
        if (!fileName.endsWith(".pas")) {
            fileName = fileName.concat(".pas");
        }
        try {
            File file = new File(fileName);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                var line = sc.nextLine();
                result.addAll(readTerminal(line));
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static List<GrammarSymbol> readTerminal(String line) {
        List<GrammarSymbol> result = new LinkedList<>();
        var lines = line.trim().split(" ");
        for (String terminalName : lines) {
            switch (terminalName) {
                case "begin":
                    result.add(buildTerminalBegin());
                    break;
                case "end":
                    result.add(buildTerminalEnd());
                    break;
                case ";":
                    result.add(buildTerminalSemicolon());
                    break;
                case "a":
                case "b":
                case "c":
                case "d":
                case "e":
                    result.add(buildTerminalId());
                    break;
                case ":=":
                    result.add(buildTerminalIs());
                    break;
                case "0":
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                    result.add(buildTerminalConst());
                    break;
                case "(":
                    result.add(buildTerminalLParen());
                    break;
                case ")":
                    result.add(buildTerminalRParen());
                    break;
                case "<":
                    result.add(buildTerminalLess());
                    break;
                case "<=":
                    result.add(buildTerminalLessEqual());
                    break;
                case "=":
                    result.add(buildTerminalEqual());
                    break;
                case "<>":
                    result.add(buildTerminalNotEqual());
                    break;
                case ">":
                    result.add(buildTerminalGreat());
                    break;
                case ">=":
                    result.add(buildTerminalGreatEqual());
                    break;
                case "+":
                    result.add(buildTerminalAdd());
                    break;
                case "-":
                    result.add(buildTerminalSub());
                    break;
                case "*":
                    result.add(buildTerminalMul());
                    break;
                case "/":
                    result.add(buildTerminalDiv());
                    break;
                default:
                    if (!terminalName.isEmpty()) {
                        System.out.println("Incorrect symbol " + terminalName);
                    }
            }
        }
        return result;
    }
}
