package com.example.easyrent.utils;

import java.text.DecimalFormat;

public class MoneyUtils {
    private static final DecimalFormat df = new DecimalFormat("#.#");

    public static String formatMoney(int amount) {
        if (amount >= 1000000) {
            return df.format(amount / 1000000.0) + " triệu/tháng";
        } else if (amount >= 1000) {
            return df.format(amount / 1000.0) + " nghìn/tháng";
        } else {
            return amount + " đồng/tháng";
        }
    }
}
