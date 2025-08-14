package br.com.fabriciodev.converter.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

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
@Table(name = "conversao_historico", schema = "fx")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conversao", nullable = false)
    private Long id_conversao;

    @Column(name = "co_moeda_origem", length = 3, nullable = false)
    private String co_moeda_origem;

    @Column(name = "co_moeda_destino", length = 3, nullable = false)
    private String co_moeda_destino;

    @Column(name = "vl_montante", nullable = false, precision = 20, scale = 10)
    private BigDecimal vl_montante;

    @Column(name = "vl_taxa", nullable = false, precision = 20, scale = 10)
    private BigDecimal vl_taxa;

    @Column(name = "vl_resultado", nullable = false, precision = 20, scale = 10)
    private BigDecimal vl_resultado;

    @Column(name = "dt_conversao", nullable = false)
    private OffsetDateTime dt_conversao = OffsetDateTime.now();

    @Column(name = "ds_ip_cliente")
    private String ds_ip_cliente;

    @Column(name = "ds_user_agent")
    private String ds_user_agent;

    @Column(name = "id_usuario")
    private Integer id_usuario;
}
