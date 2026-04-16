package br.com.arcelino.bookcatalogapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private LivroMapper livroMapper;

    @InjectMocks
    private LivroService livroService;

    @Test
    void getLivrosPorFiltros_shouldReturnMappedPage() {
        var filter = new LivroFilter("Senhor", "Tolkien", "Fantasia");
        var pageable = PageRequest.of(0, 10);

        var genero = Genero.builder().id(1L).nome("Fantasia").build();
        var livro = Livro.builder().id(1L).titulo("O Senhor dos Anéis").autor("Tolkien").genero(genero).build();
        var page = new PageImpl<>(java.util.List.of(livro), pageable, 1);

        when(livroRepository.buscarLivrosPorFiltros(filter, pageable)).thenReturn(page);
        when(livroMapper.toResponse(livro)).thenReturn(new LivroResponse(1L, "O Senhor dos Anéis", "Tolkien", "Fantasia"));

        Page<LivroResponse> result = livroService.getLivrosPorFiltros(filter, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("O Senhor dos Anéis", result.getContent().getFirst().titulo());
        assertEquals("Fantasia", result.getContent().getFirst().genero());
    }

    @Test
    void getLivroDetails_whenFound_shouldReturnDetails() {
        var genero = Genero.builder().id(1L).nome("Fantasia").build();
        var livro = Livro.builder().id(1L).titulo("Livro").autor("Autor").isbn("123").anoPublicacao(2024)
                .preco(BigDecimal.TEN).genero(genero).build();
        var details = new LivroDetails("Livro", "Autor", "123", 2024, BigDecimal.TEN, "Fantasia");

        when(livroRepository.findById(1L)).thenReturn(Optional.of(livro));
        when(livroMapper.toDetails(livro)).thenReturn(details);

        var result = livroService.getLivroDetails(1L);

        assertEquals("Livro", result.titulo());
        assertEquals("Fantasia", result.genero());
    }

    @Test
    void getLivroDetails_whenNotFound_shouldThrowException() {
        when(livroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(LivroNotFoundException.class, () -> livroService.getLivroDetails(99L));
    }

    @Test
    void criarLivro_shouldSaveAndReturnResponse() {
        var request = new LivroRequest("Livro", "Autor", "123456", 2024, BigDecimal.valueOf(59.9), 1L);
        var toSave = Livro.builder().titulo("Livro").autor("Autor").isbn("123456").build();
        var saved = Livro.builder().id(10L).titulo("Livro").autor("Autor").isbn("123456")
                .genero(Genero.builder().id(1L).nome("Fantasia").build()).build();
        var response = new LivroResponse(10L, "Livro", "Autor", "Fantasia");

        when(livroMapper.toEntity(request)).thenReturn(toSave);
        when(livroRepository.save(toSave)).thenReturn(saved);
        when(livroMapper.toResponse(saved)).thenReturn(response);

        var result = livroService.criarLivro(request);

        assertEquals(10L, result.id());
        verify(livroRepository).save(toSave);
        assertEquals(1L, toSave.getGenero().getId());
    }

    @Test
    void criarLivro_whenSaveViolatesIntegrity_shouldThrowInvalidRequest() {
        var request = new LivroRequest("Livro", "Autor", "123456", 2024, BigDecimal.valueOf(59.9), 1L);
        var toSave = Livro.builder().titulo("Livro").autor("Autor").isbn("123456").build();

        when(livroMapper.toEntity(request)).thenReturn(toSave);
        when(livroRepository.save(toSave)).thenThrow(new DataIntegrityViolationException("constraint"));

        assertThrows(InvalidLivroRequestException.class, () -> livroService.criarLivro(request));
    }

    @Test
    void atualizarLivro_whenFound_shouldUpdateAndReturnResponse() {
        var request = new LivroRequest("Atualizado", "Autor", "999", 2025, BigDecimal.valueOf(99.9), 2L);
        var existing = Livro.builder().id(5L).titulo("Antigo").autor("Autor").isbn("111").build();
        var saved = Livro.builder().id(5L).titulo("Atualizado").autor("Autor").isbn("999")
                .genero(Genero.builder().id(2L).nome("Drama").build()).build();

        when(livroRepository.findById(5L)).thenReturn(Optional.of(existing));
        doNothing().when(livroMapper).updateEntity(request, existing);
        when(livroRepository.save(existing)).thenReturn(saved);
        when(livroMapper.toResponse(saved)).thenReturn(new LivroResponse(5L, "Atualizado", "Autor", "Drama"));

        var result = livroService.atualizarLivro(5L, request);

        verify(livroMapper).updateEntity(request, existing);
        assertEquals(5L, result.id());
        assertEquals(2L, existing.getGenero().getId());
    }

    @Test
    void deletarLivro_whenFound_shouldDelete() {
        when(livroRepository.existsById(7L)).thenReturn(true);

        livroService.deletarLivro(7L);

        verify(livroRepository).deleteById(7L);
    }

    @Test
    void deletarLivro_whenNotFound_shouldThrowException() {
        when(livroRepository.existsById(7L)).thenReturn(false);

        assertThrows(LivroNotFoundException.class, () -> livroService.deletarLivro(7L));
    }
}
