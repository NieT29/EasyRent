package com.example.easyrent.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

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

    public static String formatCurrency(int amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        return formatter.format(amount) + "đ";
    }
}
