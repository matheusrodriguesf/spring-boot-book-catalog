package br.com.arcelino.bookcatalogapi.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.arcelino.bookcatalogapi.dto.LivroDetails;
import br.com.arcelino.bookcatalogapi.dto.LivroFilter;
import br.com.arcelino.bookcatalogapi.dto.LivroRequest;
import br.com.arcelino.bookcatalogapi.dto.LivroResponse;
import br.com.arcelino.bookcatalogapi.entity.Genero;
import br.com.arcelino.bookcatalogapi.entity.Livro;
import br.com.arcelino.bookcatalogapi.exception.InvalidLivroRequestException;
import br.com.arcelino.bookcatalogapi.exception.LivroNotFoundException;
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

    @Transactional(readOnly = true)
    public Page<LivroResponse> getLivrosPorFiltros(LivroFilter filter, Pageable pageable) {
        log.info("Buscando livros com filtros - Título: {}, Autor: {}, Gênero: {}", filter.titulo(), filter.autor(),
                filter.genero());
        return livroRepository.buscarLivrosPorFiltros(filter, pageable)
                .map(livroMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public LivroDetails getLivroDetails(Long id) {
        log.info("Buscando detalhes do livro com id: {}", id);
        return livroRepository.findById(id)
                .map(livroMapper::toDetails)
                .orElseThrow(() -> new LivroNotFoundException(id));

    }

    @Transactional
    public LivroResponse criarLivro(LivroRequest request) {
        log.info("Criando novo livro com título: {}", request.titulo());
        var livro = livroMapper.toEntity(request);
        livro.setGenero(criarReferenciaGenero(request.generoId()));

        return livroMapper.toResponse(salvarLivro(livro));
    }

    @Transactional
    public LivroResponse atualizarLivro(Long id, LivroRequest request) {
        log.info("Atualizando livro com id: {}", id);
        var livro = livroRepository.findById(id)
                .orElseThrow(() -> new LivroNotFoundException(id));

        livroMapper.updateEntity(request, livro);
        livro.setGenero(criarReferenciaGenero(request.generoId()));

        return livroMapper.toResponse(salvarLivro(livro));
    }

    @Transactional
    public void deletarLivro(Long id) {
        log.info("Deletando livro com id: {}", id);
        if (!livroRepository.existsById(id)) {
            throw new LivroNotFoundException(id);
        }
        livroRepository.deleteById(id);
    }

    private Genero criarReferenciaGenero(Long generoId) {
        return Genero.builder()
                .id(generoId)
                .build();
    }

    private Livro salvarLivro(Livro livro) {
        try {
            return livroRepository.save(livro);
        } catch (DataIntegrityViolationException ex) {
            throw new InvalidLivroRequestException();
        }
    }

}
