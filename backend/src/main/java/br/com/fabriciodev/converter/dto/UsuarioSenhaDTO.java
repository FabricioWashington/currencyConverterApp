package br.com.fabriciodev.converter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UsuarioSenhaDTO {

    @NotNull(message = "{validation.idUsuario}")
    private Integer idUsuario;

    @NotBlank(message = "{validation.dsSenha}")
    private String dsSenha;

    @NotBlank(message = "{validation.dsSenhaAnterior}")
    private String dsSenhaAnterior;

    @NotBlank(message = "{validation.dsSenhaConferir}")
    private String dsSenhaConferir;
}
