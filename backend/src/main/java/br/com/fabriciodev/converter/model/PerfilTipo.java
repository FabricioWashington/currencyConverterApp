package br.com.fabriciodev.converter.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "perfil_tipo", schema = "finance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilTipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_perfil")
    private Integer idTipoPerfil;

    @Column(name = "ds_tipo_perfil")
    private String dsTipoPerfil;
}
