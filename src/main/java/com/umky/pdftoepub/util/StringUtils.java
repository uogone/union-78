package com.umky.pdftoepub.util;

public class StringUtils {

    public static boolean isDigit(String s) {
        try {
            Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
