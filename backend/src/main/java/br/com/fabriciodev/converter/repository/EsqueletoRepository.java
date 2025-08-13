
package br.com.fabriciodev.converter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fabriciodev.converter.model.Esqueleto;

@Repository
public interface EsqueletoRepository extends JpaRepository<Esqueleto, Integer> {

}
