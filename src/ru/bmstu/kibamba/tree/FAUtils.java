package ru.bmstu.kibamba.tree;

import java.util.ArrayList;

public class FAUtils {
    public static String join(ArrayList<Integer> v, String delim)//Show the formatting of NFA properly
    {
        StringBuilder ss = new StringBuilder();
        for (int i = 0; i < v.size(); ++i) {
            if (i != 0)
                ss.append(delim);
            ss.append(v.get(i));
        }
        return ss.toString();
    }

    public static boolean isInputCharacter(char charAt) {
        return charAt == 'a' || charAt == 'b' || charAt == 'e';
    }

    public static String normalizeInputRegex(String regex) {
        StringBuilder newRegular = new StringBuilder();

        for (int i = 0; i < regex.length() - 1; i++) {
            if (isInputCharacter(regex.charAt(i)) &&
                    isInputCharacter(regex.charAt(i + 1))) {
                newRegular.append(regex.charAt(i)).append(".");
            } else if (isInputCharacter(regex.charAt(i)) && regex.charAt(i + 1) == '(') {
                newRegular.append(regex.charAt(i)).append(".");
            } else if (regex.charAt(i) == ')' && isInputCharacter(regex.charAt(i + 1))) {
                newRegular.append(regex.charAt(i)).append(".");
            } else if (regex.charAt(i) == '*' && isInputCharacter(regex.charAt(i + 1))) {
                newRegular.append(regex.charAt(i)).append(".");
            } else if (regex.charAt(i) == '+' && isInputCharacter(regex.charAt(i + 1))) {
                newRegular.append(regex.charAt(i)).append('.');
            } else if (regex.charAt(i) == '*' && regex.charAt(i + 1) == '(') {
                newRegular.append(regex.charAt(i)).append(".");
            } else if (regex.charAt(i) == ')' && regex.charAt(i + 1) == '(') {
                newRegular.append(regex.charAt(i)).append(".");
            } else {
                newRegular.append(regex.charAt(i));
            }
        }
        return "(".concat(newRegular.append(regex.charAt(regex.length() - 1)).append(")").toString());
    }
}
