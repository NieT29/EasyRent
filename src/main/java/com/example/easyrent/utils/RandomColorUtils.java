package com.example.easyrent.utils;

import java.util.Random;

public class RandomColorUtils {
    public static String getRandomColor() {
        Random random = new Random();
        int nextInt = random.nextInt(256*256*256);
        String colorCode = String.format("%06x", nextInt);
        return colorCode;
    }
}
