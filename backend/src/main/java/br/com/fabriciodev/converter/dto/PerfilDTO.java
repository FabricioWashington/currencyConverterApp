package br.com.fabriciodev.converter.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerfilDTO {
    private Integer idPerfil;
    private Integer idTipoPerfil;
    private Integer idEmpresa;
    private String dsPerfil;
    private String dsPerfilCurto;
    private Boolean inAgrupar;
    private Boolean inAtivo;
    private Integer idUsuarioCriacao;
    private Integer idUsuarioAlteracao;
    private LocalDateTime dtCriacao;
    private LocalDateTime dtAlteracao;
    private Integer idPerfilReferencia;
}
