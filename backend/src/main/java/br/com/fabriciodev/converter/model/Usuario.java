package br.com.fabriciodev.converter.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.fabriciodev.converter.config.Constants;
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
@Table(name = "usuario", schema = "finance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "no_usuario")
    private String noUsuario;

    @Column(name = "ds_email")
    private String dsEmail;

    @Column(name = "ds_senha")
    private String dsSenha;

    @Column(name = "ds_foto")
    private String dsFoto;

    @Column(name = "in_ativo")
    private Boolean inAtivo = Constants.ATIVO_BOOL;

    @Column(name = "id_usuario_criacao")
    private Integer idUsuarioCriacao;

    @Column(name = "id_usuario_alteracao")
    private Integer idUsuarioAlteracao;

    @Column(name = "in_email_valido")
    private Boolean inEmailValido = Constants.FALSE;

    @Column(name = "dt_excluir")
    private LocalDate dtExclusao;

    @Column(name = "id_cadastro_tipo")
    private Integer idCadastroTipo = Constants.ID_TIPO_CADASTRO_CURRENCY;

    @Column(name = "id_ultima_empresa")
    private Integer idUltimaEmpresa;

    @Column(name = "dt_ultimo_acesso")
    private LocalDateTime dtUltimoAcesso;

    @Column(name = "ds_celular")
    private String dsCelular;

    @Column(name = "ds_foto_externa")
    private String dsFotoExterna;

}
