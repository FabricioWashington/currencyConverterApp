package br.com.fabriciodev.converter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fabriciodev.converter.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByDsEmail(String dsEmail);

    List<Usuario> findByInAtivo(boolean inAtivo);

    Optional<Usuario> findByDsEmail(String dsEmail);
}