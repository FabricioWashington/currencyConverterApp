package br.com.fabriciodev.converter.dto;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateDTO {
    private Integer sourceId;
    private String base;
    private String quote;
    private Double rate;
    private LocalDate asOfDate;
    private OffsetDateTime fetchedAt;
}