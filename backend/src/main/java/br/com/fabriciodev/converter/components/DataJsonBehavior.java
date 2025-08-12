package br.com.fabriciodev.converter.components;

import java.util.HashMap;
import java.util.Map;

public class DataJsonBehavior {
    public static Map<String, Object> convertToOut(Map<String, Object> data) {
        // Aqui poderia aplicar regras de transformação, como formatação, filtros, etc.
        return new HashMap<>(data);
    }
}
