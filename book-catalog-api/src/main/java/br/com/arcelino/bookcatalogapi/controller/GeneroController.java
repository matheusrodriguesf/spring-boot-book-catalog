package br.com.arcelino.bookcatalogapi.controller;

import java.net.URI;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.arcelino.bookcatalogapi.dto.GeneroRequest;
import br.com.arcelino.bookcatalogapi.dto.GeneroResponse;
import br.com.arcelino.bookcatalogapi.service.GeneroService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@Validated
@RequestMapping("/generos")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GeneroController {

    GeneroService generoService;

    @GetMapping
    public List<GeneroResponse> allGeneros() {
        return generoService.allGeneros();
    }

    @GetMapping("{id}")
    public GeneroResponse getGeneroById(
            @PathVariable @Positive(message = "O id deve ser um número maior que zero") Long id) {
        return generoService.getGeneroById(id);
    }

    @PostMapping
    public ResponseEntity<GeneroResponse> criarGenero(@RequestBody @Valid GeneroRequest request) {
        var response = generoService.criarGenero(request);
        return ResponseEntity.created(URI.create("/generos/" + response.id())).body(response);
    }

    @PutMapping("{id}")
    public GeneroResponse atualizarGenero(
            @PathVariable @Positive(message = "O id deve ser um número maior que zero") Long id,
            @RequestBody @Valid GeneroRequest request) {
        return generoService.atualizarGenero(id, request);
    }

}
