package br.com.fabriciodev.converter.repository;

import java.util.Currency;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, String> {
    List<Currency> findByActiveTrueOrderByCodeAsc();
}