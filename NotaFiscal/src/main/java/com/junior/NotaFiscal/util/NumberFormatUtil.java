package com.junior.NotaFiscal.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

public final class NumberFormatUtil {

    private NumberFormatUtil() {}

    public static BigDecimal toMoney(BigDecimal v) {
        if (v == null) return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        return v.setScale(2, RoundingMode.HALF_UP);
    }

    public static String formatBR(BigDecimal v) {
        return toMoney(v).toPlainString().replace('.', ',');
    }

    public static String formatDot(BigDecimal v) { 
        return toMoney(v).toPlainString();
    }

    public static int geraNumero(int digitos) {
        int min = (int) Math.pow(10, digitos - 1);
        int max = (int) Math.pow(10, digitos) - 1;
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    public static String geraNumerosString(int length) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < length; i++) b.append(ThreadLocalRandom.current().nextInt(0, 10));
        return b.toString();
    }
}
