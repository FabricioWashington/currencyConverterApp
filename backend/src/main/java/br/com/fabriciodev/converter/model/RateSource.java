package br.com.fabriciodev.converter.model;

import java.time.OffsetDateTime;

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
@Table(name = "fonte_cotacao", schema = "fx")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fonte_cotacao")
    private Integer id_fonte_cotacao;

    @Column(name = "no_fonte", nullable = false, unique = true)
    private String no_fonte;

    @Column(name = "ds_base_url")
    private String ds_base_url;

    @Column(name = "ds_descricao")
    private String ds_descricao;

    @Column(name = "dt_criacao", nullable = false)
    private OffsetDateTime dt_criacao = OffsetDateTime.now();
}