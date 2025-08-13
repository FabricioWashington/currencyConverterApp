package br.com.fabriciodev.converter.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vw_acessos", schema = "seguranca")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VwAcessos {

    @Id
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "id_empresa")
    private Integer idEmpresa;

    @Column(name = "no_fantasia")
    private String noFantasia;

    @Column(name = "no_razao_social")
    private String noRazaoSocial;

    @Column(name = "no_usuario")
    private String noUsuario;

    @Column(name = "ds_foto")
    private String dsFoto;

    @Column(name = "dt_criacao")
    private LocalDateTime dtCriacao;

    @Column(name = "in_ativo")
    private Boolean inAtivo;

    @Column(name = "id_perfil")
    private Integer idPerfil;

    @Column(name = "ds_perfil_curto")
    private String dsPerfilCurto;
}
