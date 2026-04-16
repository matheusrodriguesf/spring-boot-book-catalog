package br.com.arcelino.bookcatalogapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.arcelino.bookcatalogapi.dto.LivroDetails;
import br.com.arcelino.bookcatalogapi.dto.LivroFilter;
import br.com.arcelino.bookcatalogapi.dto.LivroResponse;
import br.com.arcelino.bookcatalogapi.mapper.LivroMapper;
import br.com.arcelino.bookcatalogapi.repository.LivroRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LivroService {

    LivroRepository livroRepository;
    LivroMapper livroMapper;

    public Page<LivroResponse> getLivrosPorFiltros(LivroFilter filter, Pageable pageable) {
        return livroRepository.buscarLivrosPorFiltros(filter, pageable)
                .map(livroMapper::toResponse);
    }

    public LivroDetails getLivroDetails(Long id) {
        return livroRepository.findById(id)
                .map(livroMapper::toDetails)
                .orElseThrow(() -> new RuntimeException("Livro não encontrado com id: " + id));

    }

}
