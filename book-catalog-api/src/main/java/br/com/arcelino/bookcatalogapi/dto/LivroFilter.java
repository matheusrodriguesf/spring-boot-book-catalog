package br.com.arcelino.bookcatalogapi.dto;

import jakarta.validation.constraints.Size;

public record LivroFilter(
        @Size(max = 255, message = "Título não pode ter mais de 255 caracteres") String titulo,
        @Size(max = 255, message = "Autor não pode ter mais de 255 caracteres") String autor,
        @Size(max = 100, message = "Gênero não pode ter mais de 100 caracteres") String genero) {

}
