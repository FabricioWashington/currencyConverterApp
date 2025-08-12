package br.com.fabriciodev.converter.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.fabriciodev.converter.config.Constants;
import br.com.fabriciodev.converter.dto.UsuarioPerfilDTO;
import br.com.fabriciodev.converter.dto.filtro.FiltroDTO;
import br.com.fabriciodev.converter.model.UsuarioPerfil;
import br.com.fabriciodev.converter.repository.UsuarioPerfilRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioPerfilService {

    private final UsuarioPerfilRepository repository;
    private final ModelMapper modelMapper;

    public UsuarioPerfilDTO create(UsuarioPerfilDTO dto) {
        UsuarioPerfil entity = modelMapper.map(dto, UsuarioPerfil.class);
        entity.setDtUltimoAcesso(LocalDateTime.now());
        entity.setInAtivo(Constants.ATIVO_BOOL);
        return modelMapper.map(repository.save(entity), UsuarioPerfilDTO.class);
    }

    public List<UsuarioPerfilDTO> search(FiltroDTO filtro, Map<String, Object> dataFiltro) {
        return repository.findAll().stream()
                .filter(e -> e.getInAtivo() == Constants.ATIVO_BOOL)
                .map(e -> modelMapper.map(e, UsuarioPerfilDTO.class))
                .collect(Collectors.toList());
    }

    public List<UsuarioPerfilDTO> findActive() {
        return repository.findByInAtivo(Constants.ATIVO_BOOL).stream()
                .map(e -> modelMapper.map(e, UsuarioPerfilDTO.class))
                .collect(Collectors.toList());
    }

    public UsuarioPerfilDTO get(Integer id) {
        UsuarioPerfil usuario = repository.findById(id)
                .filter(u -> u.getInAtivo() == Constants.ATIVO_BOOL)
                .orElseThrow(() -> new RuntimeException(Constants.USER_NOT_FOUND));
        return modelMapper.map(usuario, UsuarioPerfilDTO.class);
    }

    public UsuarioPerfilDTO update(Integer id, UsuarioPerfilDTO dto) {
        UsuarioPerfil usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException(Constants.USER_NOT_FOUND));

        modelMapper.map(dto, usuario);
        usuario.setDtUltimoAcesso(LocalDateTime.now());

        return modelMapper.map(repository.save(usuario), UsuarioPerfilDTO.class);
    }

    public void delete(Integer id) {
        UsuarioPerfil usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException(Constants.USER_NOT_FOUND));
        usuario.setInAtivo(Constants.INATIVO_BOOL);
        usuario.setDtUltimoAcesso(LocalDateTime.now());
        repository.save(usuario);
    }
}
