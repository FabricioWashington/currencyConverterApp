package br.com.fabriciodev.converter.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "perfil", schema = "seguranca")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil")
    private Integer idPerfil;

    @Column(name = "id_tipo_perfil")
    private Integer idTipoPerfil;

    @Column(name = "id_empresa")
    private Integer idEmpresa;

    @Column(name = "ds_perfil")
    private String dsPerfil;

    @Column(name = "ds_perfil_curto")
    private String dsPerfilCurto;

    @Column(name = "in_agrupar")
    private Boolean inAgrupar;

    @Column(name = "in_ativo")
    private Boolean inAtivo;

    @Column(name = "id_usuario_criacao")
    private Integer idUsuarioCriacao;

    @Column(name = "id_usuario_alteracao")
    private Integer idUsuarioAlteracao;

    @Column(name = "dt_criacao")
    private LocalDateTime dtCriacao;

    @Column(name = "dt_alteracao")
    private LocalDateTime dtAlteracao;

    @Column(name = "id_perfil_referencia")
    private Integer idPerfilReferencia;
}
