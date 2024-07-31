package com.example.easyrent.utils;

public class AcreageUtils {
    public static String formatArea(double area) {
        if (area == (int) area) {
            return String.format("%dm²", (int) area);
        } else {
            return String.format("%.1fm²", area);
        }
    }
}
