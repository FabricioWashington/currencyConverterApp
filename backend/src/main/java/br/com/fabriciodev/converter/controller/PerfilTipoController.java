package br.com.fabriciodev.converter.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fabriciodev.converter.components.BaseApiController;
import br.com.fabriciodev.converter.components.BuildResult;
import br.com.fabriciodev.converter.components.ResponseHandler;
import br.com.fabriciodev.converter.dto.PerfilDTO;
import br.com.fabriciodev.converter.dto.retornoJson.RetornoListaJsonDTO;
import br.com.fabriciodev.converter.service.PerfilTipoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tipo-perfil")
@RequiredArgsConstructor
public class PerfilTipoController extends BaseApiController {

    private final PerfilTipoService service;

    @GetMapping()
    public ResponseEntity<?> findAll() {
        try {
            List<PerfilDTO> data = service.findAll();
            return ResponseHandler.renderJSON(
                    new RetornoListaJsonDTO(
                            BuildResult.json(data),
                            data.size()));
        } catch (Exception ex) {
            return error(ex);
        }
    }
}
