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
    private Integer id_fonte_cotacao;
    private String co_moeda_base;
    private String co_moeda_cotada;
    private Double vl_taxa;
    private LocalDate dt_referencia;
    private OffsetDateTime dt_coleta;
}