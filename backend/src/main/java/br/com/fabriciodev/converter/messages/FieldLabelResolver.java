package br.com.fabriciodev.converter.messages;

import java.util.Map;

public class FieldLabelResolver {

    private static final Map<String, String> CAMPOS = Map.of(
            "idUsuario", "ID do usuário",
            "dsSenha", "Senha",
            "dsSenhaAnterior", "Senha anterior",
            "dsSenhaConferir", "Confirmação da senha");

    public static String get(String fieldName) {
        return CAMPOS.getOrDefault(fieldName, fieldName);
    }
}
