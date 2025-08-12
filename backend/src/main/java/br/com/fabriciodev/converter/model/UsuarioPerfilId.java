package br.com.fabriciodev.converter.model;

import java.io.Serializable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPerfilId implements Serializable {
    private Integer idUsuario;
    private Integer idPerfil;
}