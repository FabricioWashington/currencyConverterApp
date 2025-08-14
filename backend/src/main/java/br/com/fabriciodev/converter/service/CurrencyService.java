package br.com.fabriciodev.converter.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fabriciodev.converter.components.AuthContext;
import br.com.fabriciodev.converter.dto.ConvertResponseDTO;
import br.com.fabriciodev.converter.dto.CurrencyDTO;
import br.com.fabriciodev.converter.dto.ExchangeRateDTO;
import br.com.fabriciodev.converter.dto.filtro.FiltroDTO;
import br.com.fabriciodev.converter.model.ConversionHistory;
import br.com.fabriciodev.converter.model.ExchangeRate;
import br.com.fabriciodev.converter.repository.ConversionHistoryRepository;
import br.com.fabriciodev.converter.repository.CurrencyRepository;
import br.com.fabriciodev.converter.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepo;
    private final ExchangeRateRepository rateRepo;
    private final ConversionHistoryRepository historyRepo;
    private final ModelMapper mapper;
    private final AuthContext auth;

    public List<CurrencyDTO> findActiveCurrencies() {
        return currencyRepo.findByInAtivoTrueOrderByCodeAsc()
                .stream().map(m -> mapper.map(m, CurrencyDTO.class))
                .collect(Collectors.toList());
    }

    public List<ExchangeRateDTO> latestByBase(String base) {
        List<ExchangeRate> list = rateRepo.findLatestByCoMoedaBase(base.toUpperCase());
        return list.stream().map(er -> {
            ExchangeRateDTO dto = new ExchangeRateDTO();
            dto.setId_fonte_cotacao(er.getFonte().getId_fonte_cotacao());
            dto.setCo_moeda_base(er.getCo_moeda_base());
            dto.setCo_moeda_cotada(er.getCo_moeda_cotada());
            dto.setVl_taxa(er.getVl_taxa().doubleValue());
            dto.setDt_referencia(er.getDt_referencia());
            dto.setDt_coleta(er.getDt_coleta());
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public ConvertResponseDTO convert(String co_moeda_origem, String co_moeda_destino, Double vl_montante,
            String ds_ip_cliente,
            String ds_user_agent) {
        String coMoedaOrigem = co_moeda_origem.toUpperCase();
        String coMoedaDestino = co_moeda_destino.toUpperCase();

        if (coMoedaOrigem.equals(coMoedaDestino)) {
            return new ConvertResponseDTO(coMoedaOrigem, coMoedaDestino, vl_montante, 1.0, vl_montante);
        }

        BigDecimal vl_taxa = rateRepo.findLatestRateValue(
                coMoedaOrigem.toUpperCase(),
                coMoedaDestino.toUpperCase());
        if (vl_taxa == null) {
            throw new IllegalArgumentException("Moeda não suportada: " + coMoedaOrigem + "->" + coMoedaDestino);
        }

        double result = BigDecimal.valueOf(vl_montante).multiply(vl_taxa).doubleValue();

        ConversionHistory h = new ConversionHistory();
        h.setCo_moeda_origem(coMoedaOrigem);
        h.setCo_moeda_destino(coMoedaDestino);
        h.setVl_montante(BigDecimal.valueOf(vl_montante));
        h.setVl_taxa(vl_taxa);
        h.setDs_ip_cliente(ds_ip_cliente);
        h.setDs_user_agent(ds_user_agent);
        h.setDt_conversao(OffsetDateTime.now());
        h.setId_usuario(auth.getIdUsuario());
        historyRepo.save(h);

        return new ConvertResponseDTO(coMoedaOrigem, coMoedaDestino, vl_montante, vl_taxa.doubleValue(), result);
    }

    public List<ExchangeRateDTO> searchRates(FiltroDTO filtro, Map<String, Object> dataFiltro) {
        String co_moeda_base = dataFiltro.getOrDefault("co_moeda_base", "").toString().toUpperCase();
        String co_moeda_cotada = dataFiltro.getOrDefault("co_moeda_cotada", "").toString().toUpperCase();
        Integer id_fonte_cotacao = dataFiltro.get("id_fonte_cotacao") != null
                ? Integer.parseInt(dataFiltro.get("id_fonte_cotacao").toString())
                : null;
        LocalDate dt_referencia = dataFiltro.get("dt_referencia") != null
                ? LocalDate.parse(dataFiltro.get("dt_referencia").toString())
                : null;

        List<ExchangeRate> all = rateRepo.searchFlexible(co_moeda_base, co_moeda_cotada, id_fonte_cotacao,
                dt_referencia);
        List<ExchangeRate> page = all.stream()
                .skip((long) filtro.getPage() * filtro.getPageSize())
                .limit(filtro.getPageSize())
                .collect(Collectors.toList());

        return page.stream().map(er -> {
            ExchangeRateDTO dto = new ExchangeRateDTO();
            dto.setId_fonte_cotacao(er.getFonte().getId_fonte_cotacao());
            dto.setCo_moeda_base(er.getCo_moeda_base());
            dto.setCo_moeda_cotada(er.getCo_moeda_cotada());
            dto.setVl_taxa(er.getVl_taxa().doubleValue());
            dto.setDt_referencia(er.getDt_referencia());
            dto.setDt_coleta(er.getDt_coleta());
            return dto;
        }).collect(Collectors.toList());
    }
}