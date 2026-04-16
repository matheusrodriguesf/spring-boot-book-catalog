package br.com.arcelino.bookcatalogapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public record GeneroRequest(
        @Schema(
            description = "Nome do gênero literário",
            example = "Fantasia",
            maxLength = 100)
        @NotBlank(message = "Nome do gênero é obrigatório")
        @Size(max = 100, message = "Nome do gênero não pode ter mais de 100 caracteres")
        String nome) {

}