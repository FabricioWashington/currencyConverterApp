package br.com.fabriciodev.converter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.fabriciodev.converter.dto.PerfilDTO;
import br.com.fabriciodev.converter.repository.PerfilTipoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerfilTipoService {

    private final PerfilTipoRepository repository;
    private final ModelMapper modelMapper;

    public List<PerfilDTO> findAll() {
        return repository.findAll().stream()
                .map(e -> modelMapper.map(e, PerfilDTO.class))
                .collect(Collectors.toList());
    }

}
