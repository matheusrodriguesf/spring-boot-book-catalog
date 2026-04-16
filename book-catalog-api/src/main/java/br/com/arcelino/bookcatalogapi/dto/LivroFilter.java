package br.com.arcelino.bookcatalogapi.dto;

import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public record LivroFilter(
        @Schema(
            description = "Filtro por título do livro (busca parcial)",
            example = "Senhor",
            nullable = true)
        @Size(max = 255, message = "Título não pode ter mais de 255 caracteres") String titulo,
        
        @Schema(
            description = "Filtro por nome do autor (busca parcial)",
            example = "Tolkien",
            nullable = true)
        @Size(max = 255, message = "Autor não pode ter mais de 255 caracteres") String autor,
        
        @Schema(
            description = "Filtro por gênero literário (busca parcial)",
            example = "Fantasia",
            nullable = true)
        @Size(max = 100, message = "Gênero não pode ter mais de 100 caracteres") String genero) {

}
