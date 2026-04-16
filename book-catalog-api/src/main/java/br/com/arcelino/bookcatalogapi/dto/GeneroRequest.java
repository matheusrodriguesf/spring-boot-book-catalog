package br.com.arcelino.bookcatalogapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GeneroRequest(
        @NotBlank(message = "Nome do gênero é obrigatório")
        @Size(max = 100, message = "Nome do gênero não pode ter mais de 100 caracteres")
        String nome) {

}