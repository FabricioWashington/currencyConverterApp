
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
@Table(name = "esqueleto", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Esqueleto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_esqueleto")
    private Integer idEsqueleto;

    @Column(name = "no_esqueleto")
    private String noEsqueleto;

    @Column(name = "id_usuario_alteracao")
    private Integer idUsuarioAlteracao;

    @Column(name = "id_usuario_criacao")
    private Integer idUsuarioCriacao;

    @Column(name = "dt_criacao")
    private LocalDateTime dtCriacao;

    @Column(name = "dt_alteracao")
    private LocalDateTime dtAlteracao;
}
