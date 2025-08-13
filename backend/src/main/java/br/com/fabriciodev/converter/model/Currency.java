package br.com.fabriciodev.converter.model;

import java.time.OffsetDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "currency", schema = "fx")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
    @Id
    @Column(name = "code", length = 3)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "numeric_code")
    private Integer numericCode;

    @Column(name = "scale", nullable = false)
    private Integer scale = 2;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();
}
