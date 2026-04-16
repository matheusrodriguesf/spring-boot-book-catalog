package br.com.arcelino.bookcatalogapi.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.arcelino.bookcatalogapi.dto.GeneroRequest;
import br.com.arcelino.bookcatalogapi.dto.GeneroResponse;
import br.com.arcelino.bookcatalogapi.exception.GeneroNotFoundException;
import br.com.arcelino.bookcatalogapi.mapper.GeneroMapper;
import br.com.arcelino.bookcatalogapi.repository.GeneroRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GeneroService {

    GeneroRepository generoRepository;
    GeneroMapper generoMapper;

    @Transactional(readOnly = true)
    public List<GeneroResponse> allGeneros() {
        log.info("Buscando todos os gêneros");
        return generoRepository.findAll()
                .stream()
                .map(generoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public GeneroResponse getGeneroById(Long id) {
        log.info("Buscando gênero com id: {}", id);
        return generoRepository.findById(id)
                .map(generoMapper::toResponse)
                .orElseThrow(() -> new GeneroNotFoundException(id));
    }

    @Transactional
    public GeneroResponse criarGenero(GeneroRequest request) {
        log.info("Criando novo gênero com nome: {}", request.nome());
        var genero = generoMapper.toEntity(request);
        return generoMapper.toResponse(generoRepository.save(genero));
    }

    @Transactional
    public GeneroResponse atualizarGenero(Long id, GeneroRequest request) {
        log.info("Atualizando gênero com id: {}", id);
        var genero = generoRepository.findById(id)
                .orElseThrow(() -> new GeneroNotFoundException(id));

        generoMapper.updateEntity(request, genero);

        return generoMapper.toResponse(generoRepository.save(genero));
    }

}
