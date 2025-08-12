package br.com.fabriciodev.converter.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UsuarioPerfilDTO {
    private Integer idUsuario;
    private Integer idEmpresa;
    private Integer idPerfil;
    private LocalDateTime dtUltimoAcesso;
    private Boolean inAtivo;
}
