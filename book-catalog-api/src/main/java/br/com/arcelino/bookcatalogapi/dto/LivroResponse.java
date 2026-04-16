package br.com.arcelino.bookcatalogapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LivroResponse(
        @Schema(
            description = "ID único do livro",
            example = "1")
        Long id,
        
        @Schema(
            description = "Título do livro",
            example = "O Senhor dos Anéis")
        String titulo,
        
        @Schema(
            description = "Nome do autor",
            example = "J.R.R. Tolkien")
        String autor,
        
        @Schema(
            description = "Gênero literário",
            example = "Fantasia")
        String genero) {

}
