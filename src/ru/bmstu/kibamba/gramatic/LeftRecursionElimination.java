package ru.bmstu.kibamba.gramatic;

import java.util.Scanner;

public class LeftRecursionElimination {

    // Метод для проверки, начинается ли строка str с подстроки prefix
    static boolean startsWith(String str, String prefix) {
        return str.startsWith(prefix);
    }

    // Метод для устранения левой рекурсии
    static void eliminateLeftRecursion(String production) {
        String[] parts = production.split(" → ");
        String nonTerminal = parts[0];
        String[] alternatives = parts[1].split("\\|");

        // Выделение групп
        StringBuilder beta = new StringBuilder();
        StringBuilder alpha = new StringBuilder();

        for (String alternative : alternatives) {
            if (startsWith(alternative.trim(), nonTerminal)) {
                alpha.append(alternative.substring(nonTerminal.length()).trim()).append(nonTerminal).append("'");
            } else {
                beta.append(alternative.trim()).append(nonTerminal).append("'");
            }
            beta.append(" | ");
        }

        // Вывод новых правил продукции
        System.out.println(nonTerminal + " → " + beta.toString().trim().substring(0, beta.length() - 3));
        System.out.println(nonTerminal + "' → " + alpha.toString().trim() + " | ε");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите правило продукции: ");
        String production = scanner.nextLine();
        eliminateLeftRecursion(production);
        scanner.close();
    }
}
