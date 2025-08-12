package br.com.fabriciodev.converter.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Column(name = "dt_criacao")
    protected LocalDateTime dtCriacao;

    @Column(name = "dt_alteracao")
    protected LocalDateTime dtAlteracao;

    @PrePersist
    protected void onCreate() {
        this.dtCriacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dtAlteracao = LocalDateTime.now();
    }
}