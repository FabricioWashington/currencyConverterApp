package br.com.fabriciodev.converter.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.fabriciodev.converter.config.Constants;
import br.com.fabriciodev.converter.dto.PerfilDTO;
import br.com.fabriciodev.converter.dto.TipoCartaoDTO;
import br.com.fabriciodev.converter.dto.filtro.FiltroDTO;
import br.com.fabriciodev.converter.model.Perfil;
import br.com.fabriciodev.converter.repository.PerfilRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final PerfilRepository repository;
    private final ModelMapper modelMapper;

    public List<PerfilDTO> findAll() {
        return repository.findByInAtivo(Constants.ATIVO_BOOL).stream()
                .map(e -> modelMapper.map(e, PerfilDTO.class))
                .collect(Collectors.toList());
    }

    public List<PerfilDTO> search(FiltroDTO filtro, Map<String, Object> dataFiltro) {
        return repository.findAll().stream()
                .filter(e -> {
                    boolean matches = true;

                    if (filtro.getSearch() != null && !filtro.getSearch().isEmpty()) {
                        matches = e.getDsPerfil().toLowerCase()
                                .contains(filtro.getSearch().toLowerCase());
                    }

                    if (dataFiltro != null && dataFiltro.containsKey("ativo")) {
                        boolean ativoFiltro = Boolean.parseBoolean(dataFiltro.get("ativo").toString());
                        matches = matches
                                && (e.getInAtivo() == (ativoFiltro ? Constants.ATIVO_BOOL : Constants.INATIVO));
                    }

                    return matches;
                })
                .skip((long) filtro.getPage() * filtro.getPageSize())
                .limit(filtro.getPageSize())
                .map(perfil -> modelMapper.map(perfil, PerfilDTO.class))
                .collect(Collectors.toList());
    }

    public PerfilDTO get(Integer id) {
        Perfil entity = repository.findById(id)
                .filter(e -> e.getInAtivo() == Constants.NAO_EXCLUIDO_BOOL)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));
        return modelMapper.map(entity, PerfilDTO.class);
    }

    public PerfilDTO create(PerfilDTO dto) {
        Perfil entity = modelMapper.map(dto, Perfil.class);
        entity.setInAtivo(Constants.ATIVO_BOOL);
        return modelMapper.map(repository.save(entity), PerfilDTO.class);
    }

    public PerfilDTO update(Integer id, PerfilDTO dto) {
        Perfil entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));
        modelMapper.map(dto, entity);
        entity.setInAtivo(Constants.ATIVO_BOOL);
        return modelMapper.map(repository.save(entity), PerfilDTO.class);
    }

    public void delete(Integer id) {
        Perfil entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));
        entity.setInAtivo(Constants.INATIVO_BOOL);
        repository.save(entity);
    }

    public void deletePrincipal(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Perfil não encontrada");
        }

        repository.deleteById(id);
    }
}
