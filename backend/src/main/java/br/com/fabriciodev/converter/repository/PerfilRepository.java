package br.com.fabriciodev.converter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fabriciodev.converter.model.Perfil;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
    List<Perfil> findByInAtivo(boolean inAtivo);
}
