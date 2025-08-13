package br.com.fabriciodev.converter.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "conversion_history", schema = "fx")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_currency", length = 3, nullable = false)
    private String fromCurrency;

    @Column(name = "to_currency", length = 3, nullable = false)
    private String toCurrency;

    @Column(name = "amount", nullable = false, precision = 20, scale = 10)
    private BigDecimal amount;

    @Column(name = "rate", nullable = false, precision = 20, scale = 10)
    private BigDecimal rate;

    @Column(name = "converted_at", nullable = false)
    private OffsetDateTime convertedAt = OffsetDateTime.now();

    @Column(name = "client_ip")
    private String clientIp;

    @Column(name = "user_agent")
    private String userAgent;

    // útil se quiser rastrear quem fez a conversão
    @Column(name = "id_usuario")
    private Integer idUsuario;
}
