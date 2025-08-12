package br.com.fabriciodev.util;

import br.com.fabriciodev.converter.config.Constants;
import java.util.regex.Pattern;

public class PasswordValidator {

    public static void validar(String senha) {
        if (senha == null || senha.contains(" ") || senha.trim().length() < 8) {
            throw new RuntimeException(Constants.PASSWORD_INVALID_LENGTH);
        }

        if (!Pattern.compile("[!@#\\$%\\^&*()_+\\-=\\[\\]{}|,;:'\"<>\\/?`\\.]").matcher(senha).find()) {
            throw new RuntimeException(Constants.PASSWORD_SPECIAL_CHAR);
        }

        if (!Pattern.compile("[a-z]").matcher(senha).find()
                || !Pattern.compile("[A-Z]").matcher(senha).find()) {
            throw new RuntimeException(Constants.PASSWORD_UPPERCASE_LOWERCASE);
        }

        if (!Pattern.compile("[0-9]").matcher(senha).find()) {
            throw new RuntimeException(Constants.PASSWORD_NUMBER);
        }
    }
}
