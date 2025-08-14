package br.com.fabriciodev.converter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConvertResponseDTO {
    private String co_moeda_origem;
    private String co_moeda_destino;
    private Double vl_montante;
    private Double vl_taxa;
    private Double vl_resultado;
}