
package br.com.fabriciodev.converter.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsqueletoDTO {
    private Integer idEsqueleto;
    private String noEsqueleto;
    private Integer idUsuarioCriacao;
    private Integer idUsuarioAlteracao;
    private LocalDateTime dtCriacao = LocalDateTime.now();
    private LocalDateTime dtAlteracao = LocalDateTime.now();
}
