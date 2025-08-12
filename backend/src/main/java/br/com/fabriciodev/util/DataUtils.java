package br.com.fabriciodev.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class DataUtils {

    public static String dateToOut(LocalDateTime data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return data.format(formatter);
    }

    public static LocalDate dateToIn(String data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(data, formatter);
    }

    public static long diffDiasEntreDatas(LocalDate data1, LocalDate data2) {
        return ChronoUnit.DAYS.between(data1, data2);
    }

    public static String formatMesAno(String data) {
        String[] partes = data.split("-");
        int mes = Integer.parseInt(partes[1]);
        int ano = Integer.parseInt(partes[0]);
        return Month.of(mes).getDisplayName(TextStyle.FULL, new Locale("pt", "BR")) + " de " + ano;
    }
}