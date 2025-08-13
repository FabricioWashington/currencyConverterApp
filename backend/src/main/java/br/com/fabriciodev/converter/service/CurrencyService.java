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
import br.com.fabriciodev.converter.model.Currency;
import br.com.fabriciodev.converter.model.ExchangeRate;
import br.com.fabriciodev.converter.model.RateSource;
import br.com.fabriciodev.converter.repository.ConversionHistoryRepository;
import br.com.fabriciodev.converter.repository.CurrencyRepository;
import br.com.fabriciodev.converter.repository.ExchangeRateRepository;
import br.com.fabriciodev.converter.repository.RateSourceRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepo;
    private final ExchangeRateRepository rateRepo;
    private final RateSourceRepository sourceRepo;
    private final ConversionHistoryRepository historyRepo;
    private final ModelMapper mapper;
    private final AuthContext auth;

    /** Lista de moedas ativas */
    public List<CurrencyDTO> findActiveCurrencies() {
        return currencyRepo.findByActiveTrueOrderByCodeAsc()
                .stream().map(m -> mapper.map(m, CurrencyDTO.class))
                .collect(Collectors.toList());
    }

    /** Últimas taxas para uma base (pega a mais recente por quote) */
    public List<ExchangeRateDTO> latestByBase(String base) {
        List<ExchangeRate> list = rateRepo.findLatestByBase(base.toUpperCase());
        return list.stream().map(er -> {
            ExchangeRateDTO dto = new ExchangeRateDTO();
            dto.setSourceId(er.getSource().getId());
            dto.setBase(er.getBase());
            dto.setQuote(er.getQuote());
            dto.setRate(er.getRate().doubleValue());
            dto.setAsOfDate(er.getAsOfDate());
            dto.setFetchedAt(er.getFetchedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    /** Conversão simples com última taxa conhecida (qualquer fonte) */
    @Transactional
    public ConvertResponseDTO convert(String from, String to, Double amount, String ip, String userAgent) {
        String f = from.toUpperCase();
        String t = to.toUpperCase();

        if (f.equals(t)) {
            return new ConvertResponseDTO(f, t, amount, 1.0, amount);
        }

        BigDecimal rate = rateRepo.findLatestRateValue(f, t)
                .orElseThrow(() -> new IllegalArgumentException("Moeda não suportada: " + f + "->" + t));

        double result = BigDecimal.valueOf(amount).multiply(rate).doubleValue();

        // registra histórico
        ConversionHistory h = new ConversionHistory();
        h.setFromCurrency(f);
        h.setToCurrency(t);
        h.setAmount(BigDecimal.valueOf(amount));
        h.setRate(rate);
        h.setClientIp(ip);
        h.setUserAgent(userAgent);
        h.setConvertedAt(OffsetDateTime.now());
        h.setIdUsuario(auth.getIdUsuario()); // se quiser rastrear usuário
        historyRepo.save(h);

        return new ConvertResponseDTO(f, t, amount, rate.doubleValue(), result);
    }

    /** Busca paginada de taxas (base, quote, data, source) */
    public List<ExchangeRateDTO> searchRates(FiltroDTO filtro, Map<String, Object> dataFiltro) {
        String base = dataFiltro.getOrDefault("base", "").toString().toUpperCase();
        String quote = dataFiltro.getOrDefault("quote", "").toString().toUpperCase();
        Integer sourceId = dataFiltro.get("sourceId") != null ? Integer.parseInt(dataFiltro.get("sourceId").toString())
                : null;
        LocalDate date = dataFiltro.get("asOfDate") != null ? LocalDate.parse(dataFiltro.get("asOfDate").toString())
                : null;

        List<ExchangeRate> all = rateRepo.searchFlexible(base, quote, sourceId, date);
        List<ExchangeRate> page = all.stream()
                .skip((long) filtro.getPage() * filtro.getPageSize())
                .limit(filtro.getPageSize())
                .collect(Collectors.toList());

        return page.stream().map(er -> {
            ExchangeRateDTO dto = new ExchangeRateDTO();
            dto.setSourceId(er.getSource().getId());
            dto.setBase(er.getBase());
            dto.setQuote(er.getQuote());
            dto.setRate(er.getRate().doubleValue());
            dto.setAsOfDate(er.getAsOfDate());
            dto.setFetchedAt(er.getFetchedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    /** Opcional: seed rápido para testes */
    @Transactional
    public void seedExampleRates() {
        RateSource src = sourceRepo.findByName("Frankfurter")
                .orElseGet(() -> sourceRepo.save(new RateSource(null, "Frankfurter",
                        "https://api.frankfurter.app", "API pública ECB", OffsetDateTime.now())));

        if (currencyRepo.count() == 0) {
            currencyRepo.save(new Currency("USD", "US Dollar", 840, 2, true, OffsetDateTime.now()));
            currencyRepo.save(new Currency("BRL", "Brazilian Real", 986, 2, true, OffsetDateTime.now()));
            currencyRepo.save(new Currency("EUR", "Euro", 978, 2, true, OffsetDateTime.now()));
        }

        rateRepo.upsert(src.getId(), "USD", "BRL", new BigDecimal("5.20"), LocalDate.now());
        rateRepo.upsert(src.getId(), "USD", "EUR", new BigDecimal("0.90"), LocalDate.now());
        rateRepo.upsert(src.getId(), "EUR", "BRL", new BigDecimal("5.78"), LocalDate.now());
    }
}