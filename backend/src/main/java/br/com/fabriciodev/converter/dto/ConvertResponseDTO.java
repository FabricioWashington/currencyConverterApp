package br.com.fabriciodev.converter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConvertResponseDTO {
    private String from;
    private String to;
    private Double amount;
    private Double rate;
    private Double result;
}