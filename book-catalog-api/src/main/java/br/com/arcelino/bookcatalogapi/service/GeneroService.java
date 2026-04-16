package br.com.arcelino.bookcatalogapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.arcelino.bookcatalogapi.dto.GeneroResponse;
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

    public List<GeneroResponse> allGeneros() {
        return generoRepository.findAll()
                .stream()
                .map(generoMapper::toResponse)
                .toList();
    }

}
