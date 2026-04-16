package br.com.arcelino.bookcatalogapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.arcelino.bookcatalogapi.dto.LivroDetails;
import br.com.arcelino.bookcatalogapi.dto.LivroFilter;
import br.com.arcelino.bookcatalogapi.dto.LivroResponse;
import br.com.arcelino.bookcatalogapi.service.LivroService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/livros")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LivroController {

    LivroService livroService;

    @GetMapping
    public Page<LivroResponse> getLivros(LivroFilter filter, Pageable pageable) {
        return livroService.getLivrosPorFiltros(filter, pageable);
    }

    @GetMapping("{id}/details")
    public LivroDetails getLivroDetails(@PathVariable Long id) {
        return livroService.getLivroDetails(id);
    }

}
