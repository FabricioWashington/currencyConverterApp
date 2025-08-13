package br.com.fabriciodev.converter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fabriciodev.converter.model.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    List<Empresa> findByCoEmpresaSituacao(String coEmpresaSituacao);
}
