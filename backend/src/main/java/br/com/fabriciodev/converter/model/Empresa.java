package br.com.fabriciodev.converter.model;

import java.time.LocalDateTime;
import java.util.Date;

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
@Table(name = "empresa", schema = "finance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Integer idEmpresa;

    @Column(name = "co_empresa_situacao")
    private String coEmpresaSituacao;

    @Column(name = "nu_cnpj_cpf")
    private String nuCnpjCpf;

    @Column(name = "no_razao_social")
    private String noRazaoSocial;

    @Column(name = "no_fantasia")
    private String noFantasia;

    @Column(name = "ds_caminho_logo")
    private String dsCaminhoLogo;

    @Column(name = "dt_criacao")
    private LocalDateTime dtCriacao;

    @Column(name = "dt_alteracao")
    private LocalDateTime dtAlteracao;

    @Column(name = "id_usuario_criacao")
    private Integer idUsuarioCriacao;

    @Column(name = "id_usuario_alteracao")
    private Integer idUsuarioAlteracao;

    @Column(name = "co_empresa_tipo")
    private String coEmpresaTipo;

    @Column(name = "dt_excluir")
    private Date dtExcluir;

    @Column(name = "ds_email_cobranca")
    private String dsEmailCobranca;

    @Column(name = "nu_tel_cobranca")
    private String nuTelCobranca;

    @Column(name = "ds_horario_funcionamento")
    private String dsHorarioFuncionamento;

    @Column(name = "nu_inscricao_estadual")
    private String nuInscricaoEstadual;

}
