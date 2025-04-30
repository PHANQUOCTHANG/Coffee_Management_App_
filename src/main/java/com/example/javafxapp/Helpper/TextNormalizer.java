package com.example.javafxapp.Helpper;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class TextNormalizer {
    public static String normalize(String text) {
        if (text == null) return "";
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        normalized = normalized.replaceAll("đ", "d").replaceAll("Đ", "D");
        return pattern.matcher(normalized).replaceAll("").toLowerCase();
    }
}
