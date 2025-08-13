package br.com.fabriciodev.converter.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fabriciodev.converter.model.ExchangeRate;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
        Optional<ExchangeRate> findFirstByBaseAndQuoteOrderByAsOfDateDescFetchedAtDescIdDesc(String base, String quote);

        List<ExchangeRate> findByBaseOrderByAsOfDateDescFetchedAtDescIdDesc(String base);

        List<ExchangeRate> findByBaseAndQuoteAndAsOfDate(String base, String quote, LocalDate asOfDate);
}