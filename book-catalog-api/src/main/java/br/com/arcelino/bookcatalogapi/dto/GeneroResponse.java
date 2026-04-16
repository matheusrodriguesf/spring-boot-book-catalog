package br.com.arcelino.bookcatalogapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record GeneroResponse(
        @Schema(
            description = "ID único do gênero",
            example = "1")
        Long id,
        
        @Schema(
            description = "Nome do gênero literário",
            example = "Fantasia")
        String nome) {

}
