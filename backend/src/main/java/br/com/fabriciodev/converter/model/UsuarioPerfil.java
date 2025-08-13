package br.com.fabriciodev.converter.model;

import java.time.LocalDateTime;

import br.com.fabriciodev.converter.config.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario_perfil", schema = "seguranca")
@IdClass(UsuarioPerfilId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPerfil {

    @Id
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "id_empresa")
    private Integer idEmpresa;

    @Id
    @Column(name = "id_perfil")
    private Integer idPerfil;

    @Column(name = "dt_ultimo_acesso")
    private LocalDateTime dtUltimoAcesso;

    @Column(name = "in_ativo")
    private Boolean inAtivo = Constants.ATIVO_BOOL;
}
