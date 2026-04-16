package br.com.arcelino.bookcatalogapi.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

public record LivroDetails(
        @Schema(
            description = "Título do livro",
            example = "O Senhor dos Anéis")
        String titulo,
        
        @Schema(
            description = "Nome do autor",
            example = "J.R.R. Tolkien")
        String autor,
        
        @Schema(
            description = "ISBN do livro",
            example = "978-8599295174")
        String isbn,
        
        @Schema(
            description = "Ano de publicação",
            example = "1954",
            nullable = true)
        Integer anoPublicacao,
        
        @Schema(
            description = "Preço do livro em BRL",
            example = "89.90")
        BigDecimal preco,
        
        @Schema(
            description = "Gênero literário",
            example = "Fantasia")
        String genero) {

}
