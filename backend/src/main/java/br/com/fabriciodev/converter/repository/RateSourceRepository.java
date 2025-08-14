package br.com.fabriciodev.converter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fabriciodev.converter.model.RateSource;

@Repository
public interface RateSourceRepository extends JpaRepository<RateSource, Integer> {
    Optional<RateSource> findByNoFonte(String no_fonte);
}