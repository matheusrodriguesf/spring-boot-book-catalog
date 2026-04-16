package br.com.arcelino.bookcatalogapi.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record LivroRequest(
        @NotBlank @Size(max = 255) String titulo,
        @NotBlank @Size(max = 255) String autor,
        @NotBlank @Size(max = 20) String isbn,
        Integer anoPublicacao,
        @DecimalMin(value = "0.0", inclusive = false) BigDecimal preco,
        @NotNull @Positive Long generoId) {

}
