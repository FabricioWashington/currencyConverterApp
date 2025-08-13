package br.com.fabriciodev.converter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fabriciodev.converter.model.ConversionHistory;

@Repository
public interface ConversionHistoryRepository extends JpaRepository<ConversionHistory, Long> {
    List<ConversionHistory> findAllByOrderByConvertedAtDesc();
}