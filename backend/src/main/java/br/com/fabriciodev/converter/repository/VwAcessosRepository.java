package br.com.fabriciodev.converter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fabriciodev.converter.model.VwAcessos;

public interface VwAcessosRepository extends JpaRepository<VwAcessos, Integer> {

    List<VwAcessos> findByInAtivo(boolean inAtivo);

    Optional<VwAcessos> findFirstByIdUsuario(Integer idUsuario);
}
