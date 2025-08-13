package br.com.fabriciodev.converter.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.fabriciodev.converter.components.BaseApiController;
import br.com.fabriciodev.converter.components.BuildResult;
import br.com.fabriciodev.converter.components.ResponseHandler;
import br.com.fabriciodev.converter.dto.ConvertResponseDTO;
import br.com.fabriciodev.converter.dto.CurrencyDTO;
import br.com.fabriciodev.converter.dto.ExchangeRateDTO;
import br.com.fabriciodev.converter.dto.filtro.FiltroDTO;
import br.com.fabriciodev.converter.dto.retornoJson.RetornoListaJsonDTO;
import br.com.fabriciodev.converter.dto.retornoJson.RetornoSucessoJsonDTO;
import br.com.fabriciodev.converter.messages.GenericMessages;
import br.com.fabriciodev.converter.service.CurrencyService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController extends BaseApiController {

    private final CurrencyService service;

    /** Lista moedas ativas (símbolos) */
    @GetMapping("/symbols")
    public ResponseEntity<?> symbols() {
        try {
            List<CurrencyDTO> data = service.findActiveCurrencies();
            return ResponseHandler.renderJSON(new RetornoListaJsonDTO(BuildResult.json(data), data.size()));
        } catch (Exception ex) {
            return error(ex);
        }
    }

    /** Últimas taxas para uma base (ex.: base=USD) */
    @GetMapping("/rates/{base}")
    public ResponseEntity<?> latestByBase(@PathVariable String base) {
        try {
            List<ExchangeRateDTO> data = service.latestByBase(base);
            return ResponseHandler.renderJSON(new RetornoListaJsonDTO(BuildResult.json(data), data.size()));
        } catch (Exception ex) {
            return error(ex);
        }
    }

    /** Converter valores: /convert?from=USD&to=BRL&amount=123.45 */
    @GetMapping("/convert")
    public ResponseEntity<?> convert(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam Double amount,
            @RequestHeader(value = "User-Agent", required = false) String userAgent) {
        try {
            ConvertResponseDTO res = service.convert(from, to, amount, getClientIp(), userAgent);
            return ResponseHandler.renderJSON(BuildResult.json(res));
        } catch (Exception ex) {
            return error(ex);
        }
    }

    /** Busca paginada/filtrada de taxas (ex.: base, quote, data) */
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

    /** Reprocessar/seed (opcional), útil para testes manuais */
    @PostMapping("/seed")
    public ResponseEntity<?> seed() {
        try {
            service.seedExampleRates();
            return ResponseHandler.renderJSON(
                    new RetornoSucessoJsonDTO(GenericMessages.get("generic.sucesso")));
        } catch (Exception ex) {
            return error(ex);
        }
    }
}