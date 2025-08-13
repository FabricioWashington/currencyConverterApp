package br.com.fabriciodev.converter.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exchange_rate", schema = "fx")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // fk
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_id", nullable = false)
    private RateSource source;

    @Column(name = "base", length = 3, nullable = false)
    private String base;

    @Column(name = "quote", length = 3, nullable = false)
    private String quote;

    @Column(name = "rate", nullable = false, precision = 20, scale = 10)
    private BigDecimal rate;

    @Column(name = "as_of_date", nullable = false)
    private LocalDate asOfDate;

    @Column(name = "fetched_at", nullable = false)
    private OffsetDateTime fetchedAt = OffsetDateTime.now();
}