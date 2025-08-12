package br.com.fabriciodev.converter.config;

import java.time.format.DateTimeFormatter;

public class Constants {

    public static final int ID_TIPO_CADASTRO_CURRENCY = 1;
    public static final int ID_TIPO_CADASTRO_GOOGLE = 2;
    public static final int ID_TIPO_CADASTRO_FACEBOOK = 3;

    public static final String EMPRESA_ATIVO = "A";
    public static final String EMPRESA_BLOQUEADA = "B";
    public static final String EMPRESA_ANALISE = "E";
    public static final String EMPRESA_INATIVA = "I";

    public static final int ATIVO = 1;
    public static final int INATIVO = 0;

    public static final String ATIVO_STR = "Ativo";
    public static final String INATIVO_STR = "Inativo";

    public static final boolean TRUE = true;
    public static final boolean FALSE = false;

    public static final boolean ATIVO_BOOL = true;
    public static final boolean INATIVO_BOOL = false;

    public static final boolean EXCLUIDO_BOOL = true;
    public static final boolean NAO_EXCLUIDO_BOOL = false;

    public static final int SIM_EXCLUIDO = 1;
    public static final int NAO_EXCLUIDO = 0;

    public static final boolean PAGO_BOOL = true;
    public static final boolean NAO_PAGO_BOOL = false;

    public static final String CAMPO_VAZIO = "-";

    public static final String PERFIL_ADMIN = "ADMIN";
    public static final int ID_PERFIL_ADMIN = 1;

    public static final DateTimeFormatter DATE_OUT_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter DATE_TIME_OUT_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    public static final DateTimeFormatter DATE_OUT_FORMAT_NEW = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter DATE_TIME_OUT_FORMAT_NEW = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public static final DateTimeFormatter DATE_IN_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter DATE_TIME_IN_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_TIME_IN_FORMAT_API = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public static final String CO_PESSOA_FISICA = "F";
    public static final String CO_PESSOA_JURIDICA = "J";

    public static final String EMPRESA_TIPO_CPF = "PF";
    public static final String EMPRESA_TIPO_CNPJ = "PJ";

    public static final int ID_USUARIO_SISTEMA = 3;

    public static final String USER_NOT_FOUND = "Usuário não encontrado";
    public static final String USER_NOT_ACTIVE = "Usuário não está ativo";
    public static final String USER_ALREADY_EXISTS = "Usuário já existe com o e-mail informado";
    public static final String USER_INVALID_PASSWORD = "Senha inválida";
    public static final String USER_INVALID_EMAIL = "E-mail inválido";
    public static final String USER_INVALID_CPF = "CPF inválido";
    public static final String USER_INVALID_CNPJ = "CNPJ inválido";
    public static final String USER_INVALID_PHONE = "Telefone inválido";
    public static final String USER_INVALID_NAME = "Nome inválido";
    public static final String USER_INVALID_COMPANY = "Empresa inválida";
    public static final String USER_INVALID_PROFILE = "Perfil inválido";
    public static final String USER_INVALID_TYPE = "Tipo de usuário inválido";
    public static final String USER_INVALID_CREDENTIALS = "Credenciais inválidas";
    public static final String USER_INVALID_TOKEN = "Token inválido";

    public static final String USER_INVALID_PASSWORD_LENGTH = "A senha deve ter entre 6 e 20 caracteres";
    public static final String USER_INVALID_EMAIL_FORMAT = "O e-mail deve ser válido";

    public static final String EMAIL_REQUIRED = "O e-mail é obrigatório.";
    public static final String EMAIL_ALREADY_EXISTS = "O e-mail informado já está cadastrado.";

    public static final String PASSWORD_RESET_SUCCESS = "Senha redefinida com sucesso!";

    public static final String PASSWORD_INCORRECT = "Senha Atual não confere!";
    public static final String PASSWORD_SAME = "Nova Senha não pode ser igual à senha anterior!";
    public static final String PASSWORD_MISMATCH = "Nova Senha e Confirmação não conferem!";
    public static final String PASSWORD_INVALID_LENGTH = "Senha inválida: mínimo 8 caracteres e sem espaços";
    public static final String PASSWORD_SPECIAL_CHAR = "Senha deve conter pelo menos um caractere especial.";
    public static final String PASSWORD_UPPERCASE_LOWERCASE = "Senha deve conter letras maiúsculas e minúsculas.";
    public static final String PASSWORD_NUMBER = "Senha deve conter pelo menos um número.";
    public static final String PASSWORD_INVALID = "A senha informada não atende aos requisitos mínimos.";
    public static final String PASSWORD_REQUIRED = "A senha é obrigatória.";
    public static final String PASSWORD_CONFIRMATION_MISMATCH = "A senha e a confirmação não coincidem.";

}
