package br.com.fabriciodev.converter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDTO {
    private String co_moeda;
    private String no_moeda;
    private Integer nu_codigo_numero;
    private Integer nu_casas_decimais;
    private Boolean in_ativo;
}
