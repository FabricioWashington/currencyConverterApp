package br.com.fabriciodev.converter.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fabriciodev.converter.components.BaseApiController;
import br.com.fabriciodev.converter.components.BuildResult;
import br.com.fabriciodev.converter.components.ResponseHandler;
import br.com.fabriciodev.converter.dto.ConvertResponseDTO;
import br.com.fabriciodev.converter.dto.CurrencyDTO;
import br.com.fabriciodev.converter.dto.ExchangeRateDTO;
import br.com.fabriciodev.converter.dto.filtro.FiltroDTO;
import br.com.fabriciodev.converter.dto.retornoJson.RetornoListaJsonDTO;
import br.com.fabriciodev.converter.service.CurrencyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController extends BaseApiController {

    private final CurrencyService service;

    @GetMapping("/symbols")
    public ResponseEntity<?> symbols() {
        try {
            List<CurrencyDTO> data = service.findActiveCurrencies();
            return ResponseHandler.renderJSON(new RetornoListaJsonDTO(BuildResult.json(data), data.size()));
        } catch (Exception ex) {
            return error(ex);
        }
    }

    @GetMapping("/rates/{base}")
    public ResponseEntity<?> latestByBase(@PathVariable String base) {
        try {
            List<ExchangeRateDTO> data = service.latestByBase(base);
            return ResponseHandler.renderJSON(new RetornoListaJsonDTO(BuildResult.json(data), data.size()));
        } catch (Exception ex) {
            return error(ex);
        }
    }

    @GetMapping("/convert")
    public ResponseEntity<?> convert(
            @RequestParam String co_moeda_origem,
            @RequestParam String co_moeda_destino,
            @RequestParam Double vl_montante,
            HttpServletRequest request,
            @RequestHeader(value = "User-Agent", required = false) String userAgent) {
        try {
            String clientIp = resolveClientIp(request);
            ConvertResponseDTO res = service.convert(
                    co_moeda_origem, co_moeda_destino, vl_montante, clientIp, userAgent);
            return ResponseHandler.renderJSON(BuildResult.json(res));
        } catch (Exception ex) {
            return error(ex);
        }
    }

    @PostMapping("/rates/search")
    public ResponseEntity<?> searchRates(@RequestBody FiltroDTO filtro) {
        try {
            Map<String, Object> dataFiltro = toObject(filtro.getData());
            List<ExchangeRateDTO> data = service.searchRates(filtro, dataFiltro);
            return ResponseHandler.renderJSON(new RetornoListaJsonDTO(BuildResult.json(data), data.size()));
        } catch (Exception ex) {
            return error(ex);
        }
    }

    // ---------------- util ----------------
    private String resolveClientIp(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For", "X-Real-IP", "CF-Connecting-IP",
                "X-Client-IP", "Forwarded"
        };
        for (String h : headers) {
            String v = request.getHeader(h);
            if (v != null && !v.isBlank()) {
                return v.split(",")[0].trim();
            }
        }
        return request.getRemoteAddr();
    }
}