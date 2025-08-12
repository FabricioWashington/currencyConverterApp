
package br.com.fabriciodev.converter.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.fabriciodev.converter.components.AuthContext;
import br.com.fabriciodev.converter.config.Constants;
import br.com.fabriciodev.converter.dto.EsqueletoDTO;
import br.com.fabriciodev.converter.dto.filtro.FiltroDTO;
import br.com.fabriciodev.converter.model.Esqueleto;
import br.com.fabriciodev.converter.repository.EsqueletoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EsqueletoService {

    private final EsqueletoRepository repository;
    private final AuthContext authContext;
    private final ModelMapper modelMapper;

    public List<EsqueletoDTO> findAll() {
        return repository.findAll().stream()
                .map(e -> modelMapper.map(e, EsqueletoDTO.class))
                .collect(Collectors.toList());
    }

    public List<EsqueletoDTO> search(FiltroDTO filtro, Map<String, Object> dataFiltro) {
        return repository.findAll().stream()
                .filter(e -> {
                    boolean matches = true;

                    if (filtro.getSearch() != null && !filtro.getSearch().isEmpty()) {
                        matches = e.getNoEsqueleto().toLowerCase()
                                .contains(filtro.getSearch().toLowerCase());
                    }

                    return matches;
                })
                .skip((long) filtro.getPage() * filtro.getPageSize())
                .limit(filtro.getPageSize())
                .map(esqueleto -> modelMapper.map(esqueleto, EsqueletoDTO.class))
                .collect(Collectors.toList());
    }

    public List<EsqueletoDTO> findActive() {
        return repository.findAll().stream()
                .map(e -> modelMapper.map(e, EsqueletoDTO.class))
                .collect(Collectors.toList());
    }

    public EsqueletoDTO get(Integer id) {
        Esqueleto entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Esqueleto não encontrado"));
        return modelMapper.map(entity, EsqueletoDTO.class);
    }

    public EsqueletoDTO create(EsqueletoDTO dto) {
        Esqueleto entity = modelMapper.map(dto, Esqueleto.class);
        entity.setDtCriacao(LocalDateTime.now());
        entity.setIdUsuarioCriacao(authContext.getIdUsuario());
        return modelMapper.map(repository.save(entity), EsqueletoDTO.class);
    }

    public EsqueletoDTO update(Integer id, EsqueletoDTO dto) {
        Esqueleto entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Esqueleto não encontrado"));
        modelMapper.map(dto, entity);
        entity.setDtAlteracao(LocalDateTime.now());
        entity.setIdUsuarioAlteracao(authContext.getIdUsuario());
        return modelMapper.map(repository.save(entity), EsqueletoDTO.class);
    }

    public void delete(Integer id) {
        Esqueleto entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Esqueleto não encontrado"));
        entity.setDtAlteracao(LocalDateTime.now());
        entity.setIdUsuarioAlteracao(authContext.getIdUsuario());
        repository.save(entity);
    }

    public void deletePrincipal(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Esqueleto não encontrada");
        }

        repository.deleteById(id);
    }
}
