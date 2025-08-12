package br.com.fabriciodev.converter.dto;

import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Integer idUsuario;
    private String noUsuario;
    private String dsEmail;
    private String dsCelular;
    private String dsFoto;
    private Boolean inExcluido;
}
