package br.com.arcelino.bookcatalogapi.controller;

import java.net.URI;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.arcelino.bookcatalogapi.dto.LivroDetails;
import br.com.arcelino.bookcatalogapi.dto.LivroFilter;
import br.com.arcelino.bookcatalogapi.dto.LivroRequest;
import br.com.arcelino.bookcatalogapi.dto.LivroResponse;
import br.com.arcelino.bookcatalogapi.service.LivroService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Validated
@RequestMapping("/livros")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LivroController {

    LivroService livroService;

    @GetMapping
    public Page<LivroResponse> getLivros(@Valid LivroFilter filter, Pageable pageable) {
        return livroService.getLivrosPorFiltros(filter, pageable);
    }

    @GetMapping("{id}/details")
    public LivroDetails getLivroDetails(
            @PathVariable @Positive(message = "O id deve ser um número maior que zero") Long id) {
        return livroService.getLivroDetails(id);
    }

    @PostMapping
    public ResponseEntity<LivroResponse> criarLivro(@RequestBody @Valid LivroRequest request) {
        var response = livroService.criarLivro(request);
        return ResponseEntity.created(URI.create("/livros/" + response.id())).body(response);
    }

    @PutMapping("{id}")
    public LivroResponse atualizarLivro(
            @PathVariable @Positive(message = "O id deve ser um número maior que zero") Long id,
            @RequestBody @Valid LivroRequest request) {
        return livroService.atualizarLivro(id, request);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletarLivro(
            @PathVariable @Positive(message = "O id deve ser um número maior que zero") Long id) {
        livroService.deletarLivro(id);
        return ResponseEntity.noContent().build();
    }

}
