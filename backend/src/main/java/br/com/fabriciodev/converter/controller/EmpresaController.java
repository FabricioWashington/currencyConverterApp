package br.com.fabriciodev.converter.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fabriciodev.converter.components.BaseApiController;
import br.com.fabriciodev.converter.components.BuildResult;
import br.com.fabriciodev.converter.components.ResponseHandler;
import br.com.fabriciodev.converter.dto.EmpresaDTO;
import br.com.fabriciodev.converter.dto.filtro.FiltroDTO;
import br.com.fabriciodev.converter.dto.retornoJson.RetornoListaJsonDTO;
import br.com.fabriciodev.converter.dto.retornoJson.RetornoSucessoJsonDTO;
import br.com.fabriciodev.converter.messages.GenericMessages;
import br.com.fabriciodev.converter.service.EmpresaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController extends BaseApiController {

    private final EmpresaService service;

    @GetMapping()
    public ResponseEntity<?> findAll() {
        try {
            List<EmpresaDTO> data = service.findAll();
            return ResponseHandler.renderJSON(
                    new RetornoListaJsonDTO(
                            BuildResult.json(data),
                            data.size()));
        } catch (Exception ex) {
            return error(ex);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody FiltroDTO filtro) {
        try {
            Map<String, Object> dataFiltro = toObject(filtro.getData());

            List<EmpresaDTO> data = service.search(filtro, dataFiltro);

            return ResponseHandler.renderJSON(
                    new RetornoListaJsonDTO(
                            BuildResult.json(data),
                            data.size()));
        } catch (Exception ex) {
            return error(ex);
        }
    }

    @GetMapping("/ativos")
    public ResponseEntity<?> findActive() {
        try {
            List<EmpresaDTO> data = service.findActive();
            return ResponseHandler.renderJSON(
                    new RetornoListaJsonDTO(
                            BuildResult.json(data),
                            data.size()));
        } catch (Exception ex) {
            return error(ex);
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        try {
            EmpresaDTO dto = service.get(id);
            return ResponseHandler.renderJSON(BuildResult.json(dto));
        } catch (Exception ex) {
            return error(ex);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody EmpresaDTO dto) {
        try {
            EmpresaDTO Empresa = service.create(dto);
            return ResponseHandler.renderJSON(
                    new RetornoSucessoJsonDTO(GenericMessages.get("generic.sucesso"), Empresa));
        } catch (Exception ex) {
            return error(ex);
        }
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody EmpresaDTO dto) {
        try {
            EmpresaDTO Empresa = service.update(id, dto);
            return ResponseHandler.renderJSON(
                    new RetornoSucessoJsonDTO(GenericMessages.get("generic.sucesso"), Empresa));
        } catch (Exception ex) {
            return error(ex);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Integer id) {
        try {
            service.delete(id);
            return ResponseHandler.renderJSON(
                    new RetornoSucessoJsonDTO(GenericMessages.get("generic.removido")));
        } catch (Exception ex) {
            return error(ex);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            service.delete(id);
            return ResponseHandler.renderJSON(
                    new RetornoSucessoJsonDTO(GenericMessages.get("generic.removido")));
        } catch (Exception ex) {
            return error(ex);
        }
    }
}
