package br.com.fabriciodev.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class ValorUtils {

    public static String formatMoeda(BigDecimal valor) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return format.format(valor);
    }

    public static boolean validarCpf(String cpf) {
        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}"))
            return false;

        for (int t = 9; t < 11; t++) {
            int sum = 0;
            for (int i = 0; i < t; i++) {
                sum += (cpf.charAt(i) - '0') * (t + 1 - i);
            }
            int digit = ((10 * sum) % 11) % 10;
            if ((cpf.charAt(t) - '0') != digit)
                return false;
        }

        return true;
    }

    public static boolean validarCnpj(String cnpj) {
        cnpj = cnpj.replaceAll("\\D", "");

        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}"))
            return false;

        int[] peso1 = { 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };
        int[] peso2 = { 6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2 };

        for (int j = 0; j < 2; j++) {
            int sum = 0;
            int[] peso = j == 0 ? peso1 : peso2;
            int len = j == 0 ? 12 : 13;

            for (int i = 0; i < len; i++) {
                sum += (cnpj.charAt(i) - '0') * peso[i];
            }

            int digito = sum % 11;
            digito = (digito < 2) ? 0 : 11 - digito;

            if ((cnpj.charAt(len) - '0') != digito)
                return false;
        }

        return true;
    }

    public static String formatarCpfCnpj(String valor) {
        if (valor.length() == 11)
            return formatarCpf(valor);
        if (valor.length() == 14)
            return formatarCnpj(valor);
        return valor;
    }

    private static String formatarCpf(String cpf) {
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    private static String formatarCnpj(String cnpj) {
        return cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
    }
}
