package br.com.fabriciodev.converter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDTO {
    private String code;
    private String name;
    private Integer numericCode;
    private Integer scale;
    private Boolean active;
}
