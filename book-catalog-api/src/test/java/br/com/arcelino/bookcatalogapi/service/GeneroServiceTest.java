package br.com.arcelino.bookcatalogapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.arcelino.bookcatalogapi.dto.GeneroRequest;
import br.com.arcelino.bookcatalogapi.dto.GeneroResponse;
import br.com.arcelino.bookcatalogapi.entity.Genero;
import br.com.arcelino.bookcatalogapi.exception.GeneroNotFoundException;
import br.com.arcelino.bookcatalogapi.mapper.GeneroMapper;
import br.com.arcelino.bookcatalogapi.repository.GeneroRepository;

@ExtendWith(MockitoExtension.class)
class GeneroServiceTest {

    @Mock
    private GeneroRepository generoRepository;

    @Mock
    private GeneroMapper generoMapper;

    @InjectMocks
    private GeneroService generoService;

    @Test
    void allGeneros_shouldReturnMappedList() {
        var genero = Genero.builder().id(1L).nome("Fantasia").build();
        var response = new GeneroResponse(1L, "Fantasia");

        when(generoRepository.findAll()).thenReturn(List.of(genero));
        when(generoMapper.toResponse(genero)).thenReturn(response);

        var result = generoService.allGeneros();

        assertEquals(1, result.size());
        assertEquals("Fantasia", result.getFirst().nome());
    }

    @Test
    void getGeneroById_whenFound_shouldReturnResponse() {
        var genero = Genero.builder().id(2L).nome("Romance").build();
        var response = new GeneroResponse(2L, "Romance");

        when(generoRepository.findById(2L)).thenReturn(Optional.of(genero));
        when(generoMapper.toResponse(genero)).thenReturn(response);

        var result = generoService.getGeneroById(2L);

        assertEquals(2L, result.id());
        assertEquals("Romance", result.nome());
    }

    @Test
    void getGeneroById_whenNotFound_shouldThrowException() {
        when(generoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(GeneroNotFoundException.class, () -> generoService.getGeneroById(99L));
    }

    @Test
    void criarGenero_shouldSaveAndReturnResponse() {
        var request = new GeneroRequest("Terror");
        var entity = Genero.builder().nome("Terror").build();
        var saved = Genero.builder().id(10L).nome("Terror").build();
        var response = new GeneroResponse(10L, "Terror");

        when(generoMapper.toEntity(request)).thenReturn(entity);
        when(generoRepository.save(entity)).thenReturn(saved);
        when(generoMapper.toResponse(saved)).thenReturn(response);

        var result = generoService.criarGenero(request);

        assertEquals(10L, result.id());
        assertEquals("Terror", result.nome());
    }

    @Test
    void atualizarGenero_whenFound_shouldUpdateAndReturnResponse() {
        var request = new GeneroRequest("Drama");
        var existing = Genero.builder().id(5L).nome("Antigo").build();
        var saved = Genero.builder().id(5L).nome("Drama").build();
        var response = new GeneroResponse(5L, "Drama");

        when(generoRepository.findById(5L)).thenReturn(Optional.of(existing));
        doNothing().when(generoMapper).updateEntity(request, existing);
        when(generoRepository.save(existing)).thenReturn(saved);
        when(generoMapper.toResponse(saved)).thenReturn(response);

        var result = generoService.atualizarGenero(5L, request);

        verify(generoMapper).updateEntity(request, existing);
        assertEquals(5L, result.id());
        assertEquals("Drama", result.nome());
    }

    @Test
    void atualizarGenero_whenNotFound_shouldThrowException() {
        var request = new GeneroRequest("Drama");
        when(generoRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(GeneroNotFoundException.class, () -> generoService.atualizarGenero(123L, request));
    }
}
