package br.com.fabriciodev.converter.model;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "moeda", schema = "fx")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
    @Id
    @Column(name = "co_moeda", length = 3)
    private String co_moeda;

    @Column(name = "no_moeda", nullable = false)
    private String no_moeda;

    @Column(name = "nu_codigo_numero")
    private Integer nu_codigo_numero;

    @Column(name = "nu_casas_decimais", nullable = false)
    private Integer nu_casas_decimais = 2;

    @Column(name = "in_ativo", nullable = false)
    private Boolean in_ativo = true;

    @Column(name = "dt_criacao", nullable = false)
    private OffsetDateTime dt_criacao = OffsetDateTime.now();
}
