package br.com.fabriciodev.converter.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cotacao", schema = "fx")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cotacao")
    private Long id_cotacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_fonte_cotacao", nullable = false)
    private RateSource fonte;

    @Column(name = "co_moeda_base", length = 3, nullable = false)
    private String co_moeda_base;

    @Column(name = "co_moeda_cotada", length = 3, nullable = false)
    private String co_moeda_cotada;

    @Column(name = "vl_taxa", nullable = false, precision = 20, scale = 10)
    private BigDecimal vl_taxa;

    @Column(name = "dt_referencia", nullable = false)
    private LocalDate dt_referencia;

    @Column(name = "dt_coleta", nullable = false)
    private OffsetDateTime dt_coleta = OffsetDateTime.now();
}