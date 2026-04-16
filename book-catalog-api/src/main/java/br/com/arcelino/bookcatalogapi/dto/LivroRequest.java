package br.com.arcelino.bookcatalogapi.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public record LivroRequest(
        @Schema(
            description = "Título do livro",
            example = "O Senhor dos Anéis",
            maxLength = 255)
        @NotBlank @Size(max = 255) String titulo,
        
        @Schema(
            description = "Nome do autor",
            example = "J.R.R. Tolkien",
            maxLength = 255)
        @NotBlank @Size(max = 255) String autor,
        
        @Schema(
            description = "ISBN do livro",
            example = "978-8599295174",
            maxLength = 20)
        @NotBlank @Size(max = 20) String isbn,
        
        @Schema(
            description = "Ano de publicação do livro",
            example = "1954",
            nullable = true)
        Integer anoPublicacao,
        
        @Schema(
            description = "Preço do livro em BRL",
            example = "89.90",
            minimum = "0.01")
        @DecimalMin(value = "0.0", inclusive = false) BigDecimal preco,
        
        @Schema(
            description = "ID do gênero literário",
            example = "1")
        @NotNull @Positive Long generoId) {

}
