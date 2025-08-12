package br.com.fabriciodev.converter.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.fabriciodev.converter.config.Constants;
import br.com.fabriciodev.converter.dto.EmpresaDTO;
import br.com.fabriciodev.converter.dto.filtro.FiltroDTO;
import br.com.fabriciodev.converter.model.Empresa;
import br.com.fabriciodev.converter.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository repository;
    private final ModelMapper modelMapper;

    public List<EmpresaDTO> findAll() {
        return repository.findByCoEmpresaSituacao(Constants.EMPRESA_ATIVO).stream()
                .map(e -> modelMapper.map(e, EmpresaDTO.class))
                .collect(Collectors.toList());
    }

    public List<EmpresaDTO> search(FiltroDTO filtro, Map<String, Object> dataFiltro) {
        return repository.findAll().stream()
                .filter(e -> Constants.EMPRESA_ATIVO.equals(e.getCoEmpresaSituacao()))
                .filter(e -> {
                    boolean matches = true;

                    if (filtro.getSearch() != null && !filtro.getSearch().isEmpty()) {
                        matches = e.getNoRazaoSocial().toLowerCase()
                                .contains(filtro.getSearch().toLowerCase());
                    }

                    if (dataFiltro != null && dataFiltro.containsKey("ativo")) {
                        boolean ativoFiltro = Boolean.parseBoolean(dataFiltro.get("ativo").toString());

                        String situacaoEsperada = ativoFiltro ? Constants.EMPRESA_ATIVO : Constants.EMPRESA_INATIVA;

                        matches = matches && e.getCoEmpresaSituacao().equals(situacaoEsperada);
                    }

                    return matches;
                })
                .skip((long) filtro.getPage() * filtro.getPageSize())
                .limit(filtro.getPageSize())
                .map(empresa -> modelMapper.map(empresa, EmpresaDTO.class))
                .collect(Collectors.toList());
    }

    public List<EmpresaDTO> findActive() {
        return repository.findByCoEmpresaSituacao(Constants.EMPRESA_ATIVO).stream()
                .map(e -> modelMapper.map(e, EmpresaDTO.class))
                .collect(Collectors.toList());
    }

    public EmpresaDTO get(Integer id) {
        Empresa entity = repository.findById(id)
                .filter(e -> Constants.EMPRESA_ATIVO.equals(e.getCoEmpresaSituacao()))
                .orElseThrow(() -> new RuntimeException("Empresa não encontrado"));

        return modelMapper.map(entity, EmpresaDTO.class);
    }

    public EmpresaDTO create(EmpresaDTO dto) {
        Empresa entity = modelMapper.map(dto, Empresa.class);
        entity.setDtCriacao(LocalDateTime.now());
        entity.setCoEmpresaSituacao(Constants.EMPRESA_ATIVO);
        return modelMapper.map(repository.save(entity), EmpresaDTO.class);
    }

    public EmpresaDTO update(Integer id, EmpresaDTO dto) {
        Empresa entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrado"));
        modelMapper.map(dto, entity);
        entity.setCoEmpresaSituacao(Constants.EMPRESA_ATIVO);
        entity.setDtAlteracao(LocalDateTime.now());
        return modelMapper.map(repository.save(entity), EmpresaDTO.class);
    }

    public void delete(Integer id) {
        Empresa entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrado"));
        entity.setCoEmpresaSituacao(Constants.EMPRESA_INATIVA);
        entity.setDtAlteracao(LocalDateTime.now());
        repository.save(entity);
    }

    public void deletePrincipal(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Empresa não encontrada");
        }

        repository.deleteById(id);
    }

}
