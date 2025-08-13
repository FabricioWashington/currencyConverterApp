package br.com.fabriciodev.converter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fabriciodev.converter.model.UsuarioPerfil;

@Repository
public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfil, Integer> {
    List<UsuarioPerfil> findByIdEmpresaAndInAtivo(Integer idEmpresa, boolean inAtivo);

    List<UsuarioPerfil> findByidUsuarioAndInAtivo(Integer idUsuario, boolean inAtivo);

    List<UsuarioPerfil> findByInAtivo(boolean inAtivo);
}
