package br.com.fabriciodev.converter.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.fabriciodev.converter.model.ExchangeRate;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

        List<ExchangeRate> findLatestByCoMoedaBase(String co_moeda_base);

        @Query(value = """
                        SELECT co.vl_taxa
                        FROM fx.cotacao co
                        WHERE co.co_moeda_base   = :co_moeda_base
                          AND co.co_moeda_cotada = :co_moeda_cotada
                        ORDER BY co.dt_referencia DESC, co.dt_coleta DESC, co.id_cotacao DESC
                        LIMIT 1
                        """, nativeQuery = true)
        BigDecimal findLatestRateValue(@Param("co_moeda_base") String coMoedaOrigem,
                        @Param("co_moeda_cotada") String coMoedaDestino);

        @Query(value = """
                        SELECT co.*
                        FROM fx.cotacao co
                        WHERE (:co_moeda_base    IS NULL OR :co_moeda_base    = '' OR co.co_moeda_base   = :co_moeda_base)
                          AND (:co_moeda_cotada   IS NULL OR :co_moeda_cotada   = '' OR co.co_moeda_cotada = :co_moeda_cotada)
                          AND (:id_fonte_cotacao IS NULL                OR co.id_fonte_cotacao = :id_fonte_cotacao)
                          AND (:dt_referencia   IS NULL                OR co.dt_referencia    = :dt_referencia)
                        ORDER BY co.dt_referencia DESC, co.dt_coleta DESC, co.id_cotacao DESC
                        """, nativeQuery = true)
        List<ExchangeRate> searchFlexible(@Param("co_moeda_base") String co_moeda_base,
                        @Param("co_moeda_cotada") String co_moeda_cotada,
                        @Param("id_fonte_cotacao") Integer id_fonte_cotacao,
                        @Param("dt_referencia") LocalDate dt_referencia);

}